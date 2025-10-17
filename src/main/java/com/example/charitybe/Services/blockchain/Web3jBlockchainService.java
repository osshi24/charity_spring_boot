package com.example.charitybe.Services.blockchain;

import com.example.charitybe.Config.BlockchainProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthChainId;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.http.HttpService;
import org.springframework.beans.factory.ObjectProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Triển khai BlockchainService bằng thư viện web3j.
 * - Ghi quyên góp: tạo dữ liệu hàm theo cấu hình, ký và gửi giao dịch raw.
 * - Đọc tổng on-chain: thực hiện eth_call để lấy tổng (cents) và chuyển về BigDecimal.
 */
@Service
@RequiredArgsConstructor
public class Web3jBlockchainService implements BlockchainService {

    private final BlockchainProperties props;
    private final ObjectProvider<Web3j> web3jProvider;

    /**
     * Ghi thông tin quyên góp lên smart contract theo hàm cấu hình (mặc định: recordDonation).
     * Tham số amount sẽ được chuyển sang đơn vị cents trước khi encode.
     */
    @Override
    public String recordDonation(Long projectId,
                                 String donorAddressOrName,
                                 BigDecimal amount,
                                 String currency,
                                 String memo) throws Exception {
        if (!props.isEnabled()) {
            return null;
        }

        if (props.getRpcUrl() == null || props.getRpcUrl().isBlank()) {
            throw new IllegalStateException("Missing blockchain.rpcUrl configuration");
        }
        if (props.getPrivateKey() == null || props.getPrivateKey().isBlank()) {
            throw new IllegalStateException("Missing blockchain.privateKey configuration");
        }
        if (props.getContractAddress() == null || props.getContractAddress().isBlank()) {
            throw new IllegalStateException("Missing blockchain.contractAddress configuration");
        }

        Credentials credentials = Credentials.create(props.getPrivateKey());

        Web3j web3j = web3jProvider.getIfAvailable(() -> Web3j.build(new HttpService(props.getRpcUrl())));
        EthChainId chainIdResp;
        try {
            chainIdResp = web3j.ethChainId().send();
        } catch (java.io.IOException e) {
            throw new IllegalStateException("Cannot connect to blockchain RPC: " + e.getMessage(), e);
        }
        long chainId = chainIdResp.getChainId().longValue();

        BigInteger nonce = web3j.ethGetTransactionCount(
                credentials.getAddress(), DefaultBlockParameterName.PENDING).send().getTransactionCount();

        EthGasPrice gasPriceResp = web3j.ethGasPrice().send();
        BigInteger networkGasPrice = gasPriceResp.getGasPrice();
        BigInteger gasPrice = networkGasPrice;
        if (props.getGasPriceGwei() != null && props.getGasPriceGwei() > 0) {
            gasPrice = Convert.toWei(BigDecimal.valueOf(props.getGasPriceGwei()), Convert.Unit.GWEI).toBigInteger();
        }
        BigInteger gasLimit = props.getGasLimit() != null ? props.getGasLimit() : BigInteger.valueOf(300_000);

        List<Type> inputParams = Arrays.asList(
                new Uint256(BigInteger.valueOf(projectId)),
                new Utf8String(donorAddressOrName != null ? donorAddressOrName : ""),
                // Amount is passed as decimal scaled to cents to avoid floating issues
                new Uint256(amount.movePointRight(2).toBigInteger()),
                new Utf8String(currency != null ? currency : "VND"),
                new Utf8String(memo != null ? memo : "")
        );

        Function function = new Function(
                props.getFunctionName() != null ? props.getFunctionName() : "recordDonation",
                inputParams,
                Collections.emptyList()
        );

        String data = FunctionEncoder.encode(function);

        RawTransaction rawTx = RawTransaction.createTransaction(
                nonce,
                gasPrice,
                gasLimit,
                props.getContractAddress(),
                BigInteger.ZERO,
                data
        );

        byte[] signedMessage = TransactionEncoder.signMessage(rawTx, chainId, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        EthSendTransaction txResp = web3j.ethSendRawTransaction(hexValue).send();
        if (txResp.hasError()) {
            throw new IllegalStateException("Blockchain tx error: " + txResp.getError().getMessage());
        }
        return txResp.getTransactionHash();
    }

    /**
     * Đọc tổng số tiền quyên góp theo projectId bằng eth_call.
     * Giá trị trả về (Uint256 cents) sẽ chuyển về BigDecimal theo đơn vị tiền tệ lớn.
     */
    @Override
    public BigDecimal getProjectTotal(Long projectId) throws Exception {
        if (!props.isEnabled()) {
            throw new IllegalStateException("Blockchain integration disabled");
        }
        if (props.getRpcUrl() == null || props.getRpcUrl().isBlank()) {
            throw new IllegalStateException("Missing blockchain.rpcUrl configuration");
        }
        if (props.getContractAddress() == null || props.getContractAddress().isBlank()) {
            throw new IllegalStateException("Missing blockchain.contractAddress configuration");
        }

        Function function = new Function(
                props.getReadTotalFunctionName() != null ? props.getReadTotalFunctionName() : "getProjectTotalCents",
                Arrays.asList(new Uint256(BigInteger.valueOf(projectId))),
                Arrays.asList(new TypeReference<Uint256>() {})
        );

        String data = FunctionEncoder.encode(function);
        Transaction callTx = Transaction.createEthCallTransaction(null, props.getContractAddress(), data);
        Web3j web3j = web3jProvider.getIfAvailable(() -> Web3j.build(new HttpService(props.getRpcUrl())));
        EthCall resp;
        try {
            resp = web3j.ethCall(callTx, DefaultBlockParameterName.LATEST).send();
        } catch (java.io.IOException e) {
            throw new IllegalStateException("Cannot connect to blockchain RPC: " + e.getMessage(), e);
        }
        if (resp.hasError()) {
            throw new IllegalStateException("Blockchain call error: " + resp.getError().getMessage());
        }
        List<Type> decoded = FunctionReturnDecoder.decode(resp.getValue(), function.getOutputParameters());
        if (decoded == null || decoded.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigInteger cents = (BigInteger) decoded.get(0).getValue();
        return new BigDecimal(cents).movePointLeft(2);
    }
}

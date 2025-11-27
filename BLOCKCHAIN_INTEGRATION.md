# 🔗 Blockchain Integration Documentation

## 📖 Tổng quan

Hệ thống Charity Backend đã được tích hợp với **Sepolia Testnet** (Ethereum) để lưu trữ lịch sử giao dịch từ thiện lên blockchain, đảm bảo:
- ✅ **Minh bạch**: Mọi donation và disbursement đều được ghi nhận công khai
- ✅ **Bất biến**: Dữ liệu không thể bị thay đổi sau khi ghi
- ✅ **Audit Trail**: Lịch sử đầy đủ có thể verify bất cứ lúc nào

---

## 🏗️ Kiến trúc

### **1. Components**

```
┌─────────────────┐
│   Frontend      │
└────────┬────────┘
         │ POST /api/v1/quyen_gop
         ▼
┌─────────────────────────────────────┐
│      Spring Boot Backend            │
│  ┌──────────────────────────────┐  │
│  │   ProxyController            │  │
│  │   - Nhận request             │  │
│  │   - Check permissions        │  │
│  │   - Forward to PostgREST     │  │
│  └──────────┬───────────────────┘  │
│             │                       │
│             ▼                       │
│  ┌──────────────────────────────┐  │
│  │   PostgreSQL Database        │  │
│  │   - Lưu donation/disbursement│  │
│  │   - Return ID                │  │
│  └──────────┬───────────────────┘  │
│             │                       │
│             ▼                       │
│  ┌──────────────────────────────┐  │
│  │ AsyncBlockchainProcessor     │  │
│  │   - @Async background job    │  │
│  │   - Fetch entity từ DB       │  │
│  └──────────┬───────────────────┘  │
│             │                       │
│             ▼                       │
│  ┌──────────────────────────────┐  │
│  │   BlockchainService          │  │
│  │   - Encode function call     │  │
│  │   - Sign transaction         │  │
│  │   - Send to Sepolia          │  │
│  └──────────┬───────────────────┘  │
└─────────────┼───────────────────────┘
              │
              ▼
     ┌────────────────────┐
     │  Sepolia Testnet   │
     │  Smart Contract    │
     │  - Record data     │
     │  - Emit events     │
     └────────────────────┘
```

### **2. Workflow Chi tiết**

#### **Donation Flow:**

1. **User tạo donation** → POST `/api/v1/quyen_gop`
2. **ProxyController:**
   - Kiểm tra authentication (JWT)
   - Kiểm tra permission (người dùng thường có thể donate)
   - Forward request đến PostgREST
3. **Database:**
   - Insert record vào `quyen_gop` table
   - Return `id` của donation mới
4. **ProxyController (Response Handler):**
   - Parse response JSON để lấy `id`
   - Trigger `asyncBlockchainProcessor.processDonation(id)`
   - Return 201 Created ngay cho user (không chờ blockchain)
5. **AsyncBlockchainProcessor (Background Thread):**
   - Fetch `QuyenGop` entity từ DB
   - Gọi `blockchainService.recordDonation(quyenGop)`
   - Update `blockchain_tx_hash` và `blockchain_status = "PENDING"`
   - Wait for transaction confirmation (polling every 5s, max 2 minutes)
   - Update `blockchain_status = "CONFIRMED"` và `blockchain_block_number`
6. **BlockchainService:**
   - Encode function call: `recordDonation(id, donor, projectId, amount, ...)`
   - Get nonce từ Sepolia
   - Create raw transaction
   - Sign với private key
   - Send via `ethSendRawTransaction`
   - Return transaction hash

#### **Disbursement Flow:**

Tương tự như Donation flow, chỉ khác:
- Endpoint: POST `/api/v1/giai_ngan`
- Permission: Chỉ Admin/Operator được phép
- Function: `recordDisbursement(id, projectId, amount, type, recipient, ...)`

---

## 📁 File Structure

```
charity-be/
├── contracts/
│   ├── CharityTransactions.sol        # Smart contract code
│   └── README.md                      # Deployment guide
│
├── database-migrations/
│   └── add_blockchain_fields.sql      # SQL migration
│
├── src/main/java/com/example/charitybe/
│   ├── Config/
│   │   ├── BlockchainConfig.java      # Load properties
│   │   └── Web3jConfig.java           # Web3j beans
│   │
│   ├── Services/
│   │   ├── BlockchainService.java     # Interact with contract
│   │   └── AsyncBlockchainProcessor.java  # Async processing
│   │
│   ├── Controllers/
│   │   └── ProxyController.java       # Updated with blockchain trigger
│   │
│   └── entities/
│       ├── QuyenGop.java              # Added blockchain fields
│       └── GiaiNgan.java              # Added blockchain fields
│
└── src/main/resources/
    └── application.properties         # Blockchain configuration
```

---

## 🗄️ Database Schema Changes

### **New Columns:**

**Table: `quyen_gop`**
| Column | Type | Description |
|--------|------|-------------|
| `blockchain_tx_hash` | VARCHAR(66) | Transaction hash (0x...) |
| `blockchain_block_number` | BIGINT | Block number |
| `blockchain_timestamp` | TIMESTAMPTZ | Blockchain timestamp |
| `blockchain_status` | VARCHAR(50) | PENDING, CONFIRMED, FAILED |

**Table: `giai_ngan`**
| Column | Type | Description |
|--------|------|-------------|
| `blockchain_tx_hash` | VARCHAR(66) | Transaction hash (0x...) |
| `blockchain_block_number` | BIGINT | Block number |
| `blockchain_timestamp` | TIMESTAMPTZ | Blockchain timestamp |
| `blockchain_status` | VARCHAR(50) | PENDING, CONFIRMED, FAILED |

### **Run Migration:**
```bash
psql -h postgre.oshi.id.vn -U super_admin -d charity_db_v2 -f database-migrations/add_blockchain_fields.sql
```

---

## ⚙️ Configuration

### **application.properties**

```properties
# Enable/Disable blockchain integration
blockchain.enabled=true

# Network settings
blockchain.network.name=sepolia
blockchain.network.chain-id=11155111

# RPC endpoint
blockchain.rpc.url=https://sepolia.infura.io/v3/YOUR_PROJECT_ID

# Wallet credentials
blockchain.wallet.private-key=YOUR_PRIVATE_KEY
blockchain.wallet.address=YOUR_ADDRESS

# Deployed contract address
blockchain.contract.address=0x742d35Cc6634C0532925a3b844Bc9e7595f0bEb2

# Gas settings
blockchain.gas-price=20000000000    # 20 Gwei
blockchain.gas-limit=6721975

# Async processing
blockchain.async.enabled=true

# Retry settings
blockchain.retry.max-attempts=3
blockchain.retry.delay=5000         # milliseconds
```

### **Environment Variables (Production)**

```bash
export BLOCKCHAIN_ENABLED=true
export BLOCKCHAIN_RPC_URL="https://sepolia.infura.io/v3/..."
export BLOCKCHAIN_PRIVATE_KEY="0x..."
export BLOCKCHAIN_WALLET_ADDRESS="0x..."
export BLOCKCHAIN_CONTRACT_ADDRESS="0x..."
```

Update `application.properties`:
```properties
blockchain.enabled=${BLOCKCHAIN_ENABLED:false}
blockchain.rpc.url=${BLOCKCHAIN_RPC_URL}
blockchain.wallet.private-key=${BLOCKCHAIN_PRIVATE_KEY}
blockchain.wallet.address=${BLOCKCHAIN_WALLET_ADDRESS}
blockchain.contract.address=${BLOCKCHAIN_CONTRACT_ADDRESS}
```

---

## 🎯 Smart Contract API

### **Functions:**

#### 1. **recordDonation**
```solidity
function recordDonation(
    uint256 _id,                // Database ID
    address _donor,             // Donor wallet address
    uint256 _projectId,         // Project ID
    uint256 _amount,            // Amount donated
    string memory _currency,    // Currency (VND, ETH, etc.)
    string memory _transactionCode,  // Transaction code
    string memory _paymentMethod     // Payment method
) public onlyOwner
```

**Emits:** `DonationRecorded(id, donor, projectId, amount, transactionCode, timestamp)`

#### 2. **recordDisbursement**
```solidity
function recordDisbursement(
    uint256 _id,                // Database ID
    uint256 _projectId,         // Project ID
    uint256 _amount,            // Amount disbursed
    string memory _disbursementType,  // Type
    string memory _recipient,   // Recipient name
    string memory _purpose,     // Purpose of use
    address _approver           // Approver wallet
) public onlyOwner
```

**Emits:** `DisbursementRecorded(id, projectId, amount, recipient, approver, timestamp)`

#### 3. **Query Functions (View)**
```solidity
function getDonation(uint256 _id) public view returns (Donation memory)
function getDisbursement(uint256 _id) public view returns (Disbursement memory)
function getProjectDonations(uint256 _projectId) public view returns (uint256[] memory)
function getProjectDisbursements(uint256 _projectId) public view returns (uint256[] memory)
function donationExists(uint256 _id) public view returns (bool)
function disbursementExists(uint256 _id) public view returns (bool)
```

---

## 🧪 Testing Guide

### **1. Manual Testing**

#### **Test Donation:**
```bash
# 1. Login to get access token
POST http://localhost:5555/api/auth/login
{
  "email": "admin@example.com",
  "password": "123456"
}

# 2. Create donation
POST http://localhost:5555/api/v1/quyen_gop
Authorization: Bearer {accessToken}
{
  "maDuAn": 1,
  "soTien": 50000,
  "donViTienTe": "VND",
  "phuongThucThanhToan": "chuyen_khoan",
  "maGiaoDich": "TEST_001"
}

# 3. Check database after 30 seconds
SELECT * FROM quyen_gop WHERE id = {donation_id};

# Expected:
# - blockchain_tx_hash: 0x...
# - blockchain_status: CONFIRMED
# - blockchain_block_number: <number>
```

#### **Test Disbursement:**
```bash
# 1. Login as Admin
# 2. Create disbursement
POST http://localhost:5555/api/v1/giai_ngan
Authorization: Bearer {accessToken}
{
  "maDuAn": 1,
  "soTien": 30000,
  "loaiGiaiNgan": "tu_tro",
  "ngayGiaiNgan": "2025-11-24",
  "nguoiNhan": "Test Recipient",
  "mucDichSuDung": "Educational support"
}

# 3. Check database
SELECT * FROM giai_ngan WHERE id = {disbursement_id};
```

### **2. Verify on Blockchain**

1. Copy `blockchain_tx_hash` từ database
2. Truy cập: https://sepolia.etherscan.io/tx/{tx_hash}
3. Kiểm tra:
   - Status: Success ✅
   - To: Contract Address
   - Block: Block number
   - Input Data: Decoded method call

### **3. Query Smart Contract**

**Using Remix:**
1. Load contract at address
2. Call `getDonation(donationId)`
3. Verify data matches database

**Using Etherscan:**
1. Go to contract page
2. Tab "Read Contract"
3. Call view functions

---

## 📊 Monitoring

### **Application Logs**

```bash
# Real-time monitoring
tail -f logs/spring-boot.log | grep -i blockchain

# Important log patterns:
# - "Recording donation X on blockchain"
# - "Donation X recorded on blockchain. TxHash: 0x..."
# - "Donation X confirmed on blockchain at block Y"
# - "Error recording donation on blockchain"
```

### **Database Queries**

```sql
-- Check pending transactions
SELECT id, blockchain_tx_hash, blockchain_status, ngay_tao
FROM quyen_gop
WHERE blockchain_status = 'PENDING'
ORDER BY ngay_tao DESC;

-- Check failed transactions
SELECT id, blockchain_tx_hash, blockchain_status, ngay_tao
FROM quyen_gop
WHERE blockchain_status = 'FAILED'
ORDER BY ngay_tao DESC;

-- Statistics
SELECT
  blockchain_status,
  COUNT(*) as count
FROM quyen_gop
GROUP BY blockchain_status;
```

### **Blockchain Monitoring**

- **Transaction Status:** https://sepolia.etherscan.io/tx/{hash}
- **Contract Activity:** https://sepolia.etherscan.io/address/{contract_address}
- **Gas Tracker:** https://sepolia.etherscan.io/gastracker

---

## ⚠️ Troubleshooting

### **Issue 1: Blockchain not recording**

**Symptoms:**
- Donation created successfully
- No blockchain_tx_hash after 1 minute

**Debug:**
```bash
# Check if blockchain is enabled
grep "blockchain.enabled" src/main/resources/application.properties

# Check logs
grep "Blockchain not configured" logs/spring-boot.log

# Verify configuration
curl http://localhost:5555/actuator/configprops | jq '.blockchain'
```

**Solutions:**
- Ensure `blockchain.enabled=true`
- Verify private key is configured
- Check contract address is not placeholder

### **Issue 2: Insufficient funds**

**Symptoms:**
- Log: "Error sending transaction: insufficient funds"

**Solutions:**
1. Check wallet balance: https://sepolia.etherscan.io/address/{wallet_address}
2. Get test ETH from faucet: https://sepoliafaucet.com
3. Need ~0.01 ETH per transaction

### **Issue 3: Transaction stuck in PENDING**

**Symptoms:**
- `blockchain_status` stays PENDING after 5+ minutes

**Debug:**
```bash
# Check transaction on Etherscan
# https://sepolia.etherscan.io/tx/{tx_hash}

# Possible reasons:
# - Network congestion
# - Gas price too low
# - Nonce issues
```

**Solutions:**
- Wait longer (up to 10-15 minutes in congestion)
- Increase `blockchain.gas-price` in config
- Speed up transaction on MetaMask

### **Issue 4: Nonce too low**

**Symptoms:**
- Log: "Error sending transaction: nonce too low"

**Cause:**
- Multiple transactions sent simultaneously
- Previous transaction not confirmed

**Solutions:**
- Wait for previous transaction to confirm
- Restart application (nonce will refresh)

---

## 🔒 Security Considerations

### **1. Private Key Management**

❌ **NEVER:**
- Commit private key to Git
- Share via email/Slack
- Use personal wallet
- Store in plaintext

✅ **ALWAYS:**
- Use environment variables
- Use separate deployment wallet
- Keep minimal balance (~0.5 ETH)
- Rotate keys periodically

### **2. Git Security**

`.gitignore`:
```
application-prod.properties
application-local.properties
*.key
*.pem
.env
.env.local
```

### **3. Access Control**

- Only contract owner can record transactions
- Owner = deployer wallet address
- Transfer ownership carefully using `transferOwnership()`

---

## 📈 Performance Optimization

### **1. Async Processing**

- Blockchain recording không block HTTP response
- User nhận 201 Created ngay lập tức
- Background job xử lý blockchain trong ~30 giây

### **2. Retry Logic**

- Max 3 attempts với 5 seconds delay
- Handles temporary network issues
- Prevents data loss

### **3. Database Indexing**

```sql
-- Already created in migration
CREATE INDEX idx_quyen_gop_blockchain_tx_hash ON quyen_gop(blockchain_tx_hash);
CREATE INDEX idx_quyen_gop_blockchain_status ON quyen_gop(blockchain_status);
```

---

## 🚀 Production Deployment Checklist

- [ ] Deploy smart contract to Sepolia
- [ ] Verify contract on Etherscan
- [ ] Create deployment wallet (separate from personal)
- [ ] Fund wallet with test ETH (~1-2 ETH)
- [ ] Register Infura/Alchemy account
- [ ] Set environment variables (not in application.properties)
- [ ] Run database migration
- [ ] Test with sample donation
- [ ] Verify transaction on Etherscan
- [ ] Set up monitoring alerts
- [ ] Document contract address and wallet address
- [ ] Backup wallet private key securely

---

## 📚 Additional Resources

- **Smart Contract Deployment:** See `contracts/README.md`
- **Solidity Docs:** https://docs.soliditylang.org
- **Web3j Docs:** https://docs.web3j.io
- **Sepolia Faucet:** https://sepoliafaucet.com
- **Etherscan Sepolia:** https://sepolia.etherscan.io

---

## 🆘 Support

**Logs Location:**
- Application: `logs/spring-boot.log`
- Blockchain: Filter with `grep -i blockchain`

**Key Monitoring Points:**
1. Application startup - Check blockchain connection
2. Donation/Disbursement creation - Check trigger logs
3. Database - Monitor blockchain_status field
4. Etherscan - Verify transactions

**Contact:**
- Backend Team: [Your contact]
- DevOps: [Your contact]

---

**Document Version:** 1.0.0
**Last Updated:** 2025-11-24
**Spring Boot Version:** 3.5.6
**Web3j Version:** 4.10.3
**Solidity Version:** ^0.8.20

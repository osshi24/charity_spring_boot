# 🔗 Blockchain Integration - Smart Contract Deployment Guide

## 📋 Tổng quan

Smart contract `CharityTransactions.sol` được thiết kế để lưu trữ lịch sử giao dịch từ thiện (donations và disbursements) lên Sepolia testnet để đảm bảo tính minh bạch và không thể thay đổi.

---

## 🛠️ Công cụ cần thiết

### 1. **Remix IDE** (Khuyến nghị cho người mới bắt đầu)
- URL: https://remix.ethereum.org
- Không cần cài đặt, sử dụng trực tiếp trên browser

### 2. **MetaMask Wallet**
- Extension cho Chrome/Firefox
- Download: https://metamask.io

### 3. **Sepolia Test ETH**
- Faucet: https://sepoliafaucet.com
- Hoặc: https://faucets.chain.link/sepolia

---

## 🚀 Hướng dẫn Deploy Contract sử dụng Remix

### **Bước 1: Chuẩn bị Wallet**

1. Cài đặt MetaMask extension
2. Tạo wallet mới hoặc import existing wallet
3. Chuyển network sang **Sepolia Test Network**
   - Click vào network dropdown → Show/hide test networks → Enable
   - Chọn Sepolia
4. Lấy test ETH từ faucet (cần ~0.1 ETH để deploy)

### **Bước 2: Deploy Contract trên Remix**

1. Mở Remix IDE: https://remix.ethereum.org

2. Tạo file mới `CharityTransactions.sol` trong thư mục `contracts/`

3. Copy toàn bộ code từ file `CharityTransactions.sol` vào Remix

4. **Compile Contract:**
   - Click tab "Solidity Compiler" (icon hình chữ S)
   - Chọn compiler version: `0.8.20` hoặc cao hơn
   - Click "Compile CharityTransactions.sol"
   - Đảm bảo không có errors

5. **Deploy Contract:**
   - Click tab "Deploy & Run Transactions" (icon Ethereum)
   - Environment: Chọn **"Injected Provider - MetaMask"**
   - MetaMask sẽ popup → Connect account
   - Contract: Chọn **"CharityTransactions"**
   - Click **"Deploy"** button màu cam
   - MetaMask popup → Confirm transaction
   - Chờ transaction được confirm (~15-30 giây)

6. **Lưu Contract Address:**
   - Sau khi deploy thành công, contract sẽ xuất hiện trong "Deployed Contracts"
   - Copy địa chỉ contract (ví dụ: `0x742d35Cc6634C0532925a3b844Bc9e7595f0bEb2`)
   - **LƯU ĐỊA CHỈ NÀY** để config vào Spring Boot

### **Bước 3: Verify Contract trên Etherscan (Optional)**

1. Truy cập: https://sepolia.etherscan.io
2. Paste contract address vào search
3. Tab "Contract" → "Verify and Publish"
4. Điền thông tin:
   - Compiler Type: Solidity (Single file)
   - Compiler Version: 0.8.20+commit.a1b79de6
   - License: MIT
5. Paste source code từ `CharityTransactions.sol`
6. Submit → Contract được verify

---

## ⚙️ Cấu hình Spring Boot Application

### **Bước 1: Update Application Properties**

Mở file `src/main/resources/application.properties` và update các thông tin sau:

```properties
# Blockchain Configuration (Sepolia Testnet)
blockchain.enabled=true
blockchain.network.name=sepolia
blockchain.network.chain-id=11155111

# Infura RPC URL (Đăng ký tại https://infura.io để lấy PROJECT_ID)
blockchain.rpc.url=https://sepolia.infura.io/v3/YOUR_INFURA_PROJECT_ID

# Wallet Private Key (Export từ MetaMask)
# ⚠️ CẢNH BÁO: KHÔNG COMMIT PRIVATE KEY LÊN GIT!
blockchain.wallet.private-key=YOUR_PRIVATE_KEY_HERE

# Wallet Address (Public address của wallet)
blockchain.wallet.address=YOUR_WALLET_ADDRESS_HERE

# Contract Address (Địa chỉ contract vừa deploy)
blockchain.contract.address=0x742d35Cc6634C0532925a3b844Bc9e7595f0bEb2

# Gas Settings (Có thể giữ nguyên hoặc điều chỉnh)
blockchain.gas-price=20000000000       # 20 Gwei
blockchain.gas-limit=6721975           # Default limit

# Async Settings
blockchain.async.enabled=true
blockchain.retry.max-attempts=3
blockchain.retry.delay=5000            # 5 seconds
```

### **Bước 2: Lấy Infura Project ID**

1. Đăng ký tài khoản tại: https://infura.io
2. Tạo new project → Chọn **"Web3 API"**
3. Copy **Project ID** (ví dụ: `9aa3d95b3bc440fa88ea12eaa4456161`)
4. Update vào `blockchain.rpc.url`:
   ```
   blockchain.rpc.url=https://sepolia.infura.io/v3/9aa3d95b3bc440fa88ea12eaa4456161
   ```

**Alternative RPC URLs** (không cần Infura):
- Alchemy: `https://eth-sepolia.g.alchemy.com/v2/YOUR_API_KEY`
- Public RPC: `https://rpc.sepolia.org` (có thể chậm/không ổn định)

### **Bước 3: Export Private Key từ MetaMask**

⚠️ **CẢNH BÁO BẢO MẬT:**
- Private key cho phép kiểm soát TOÀN BỘ tiền trong wallet
- KHÔNG BAO GIỜ chia sẻ private key
- KHÔNG commit private key lên Git
- Chỉ dùng wallet test với số dư nhỏ

**Cách export:**
1. Mở MetaMask
2. Click 3 dots → Account details
3. Click "Export Private Key"
4. Nhập password → Confirm
5. Copy private key (bắt đầu bằng `0x...`)
6. Paste vào `blockchain.wallet.private-key` (giữ lại `0x` prefix hoặc bỏ đi đều được)

### **Bước 4: Chạy Database Migration**

Thêm blockchain columns vào database:

```bash
# Connect to PostgreSQL
psql -h postgre.oshi.id.vn -U super_admin -d charity_db_v2

# Run migration script
\i database-migrations/add_blockchain_fields.sql

# Verify columns added
\d quyen_gop
\d giai_ngan
```

Hoặc chạy script trực tiếp:
```bash
psql -h postgre.oshi.id.vn -U super_admin -d charity_db_v2 -f database-migrations/add_blockchain_fields.sql
```

### **Bước 5: Build & Run Application**

```bash
# Build project
mvn clean install

# Run application
mvn spring-boot:run

# Hoặc
java -jar target/charity-be-0.0.1-SNAPSHOT.jar
```

Kiểm tra logs để đảm bảo blockchain connection thành công:
```
Connected to Ethereum network: Geth/v1.10.23-stable-...
Loaded wallet address: 0x742d35Cc...
Gas Provider configured - Price: 20000000000 wei, Limit: 6721975
```

---

## 🧪 Test Blockchain Integration

### **Test 1: Tạo Donation**

```bash
POST http://localhost:5555/api/v1/quyen_gop
Authorization: Bearer YOUR_ACCESS_TOKEN
Content-Type: application/json

{
  "maDuAn": 1,
  "soTien": 100000,
  "donViTienTe": "VND",
  "phuongThucThanhToan": "chuyen_khoan",
  "maGiaoDich": "TXN123456",
  "loiNhan": "Donation for charity project"
}
```

**Expected:**
1. Response 201 Created với donation ID
2. Donation được lưu vào DB ngay lập tức
3. Background job trigger blockchain recording
4. Sau ~30 giây, check database:
   ```sql
   SELECT id, blockchain_tx_hash, blockchain_status, blockchain_block_number
   FROM quyen_gop
   WHERE id = <donation_id>;
   ```
5. `blockchain_status` sẽ chuyển từ `null` → `PENDING` → `CONFIRMED`

### **Test 2: Verify trên Etherscan**

1. Copy `blockchain_tx_hash` từ database
2. Truy cập: https://sepolia.etherscan.io/tx/{tx_hash}
3. Kiểm tra transaction details:
   - Status: Success
   - To: Contract Address
   - Input Data: Encoded function call

### **Test 3: Query Data từ Smart Contract**

Sử dụng Remix hoặc Etherscan:

1. Vào Deployed Contract trên Remix
2. Gọi function `getDonation(donationId)`
3. Verify dữ liệu khớp với database

---

## 📊 Monitoring & Debugging

### **Check Logs**

```bash
# Tail application logs
tail -f logs/charity-be.log | grep -i blockchain

# Hoặc trong console
mvn spring-boot:run | grep -i blockchain
```

**Log messages quan trọng:**
- `"Recording donation X on blockchain"` - Bắt đầu ghi
- `"Donation X recorded on blockchain. TxHash: 0x..."` - Gửi transaction thành công
- `"Donation X confirmed on blockchain at block Y"` - Transaction confirmed
- `"Error recording donation on blockchain"` - Có lỗi xảy ra

### **Common Issues**

#### 1. **"Blockchain credentials not configured"**
- Check `blockchain.wallet.private-key` trong `application.properties`
- Đảm bảo không phải placeholder `YOUR_PRIVATE_KEY_HERE`

#### 2. **"Contract address not configured"**
- Check `blockchain.contract.address` trong `application.properties`
- Đảm bảo contract đã được deploy thành công

#### 3. **"Error sending transaction: insufficient funds"**
- Wallet không đủ test ETH
- Lấy thêm từ faucet: https://sepoliafaucet.com

#### 4. **"Transaction not mined yet" (Timeout)**
- Network congestion → Tăng gas price
- Hoặc chờ lâu hơn (~2-3 phút)

#### 5. **"Nonce too low"**
- Đã có transaction pending với nonce này
- Chờ transaction cũ confirm hoặc cancel nó trên MetaMask

---

## 🔒 Security Best Practices

### **1. Quản lý Private Key**

**✅ ĐÚNG:**
- Lưu trong environment variables:
  ```bash
  export BLOCKCHAIN_PRIVATE_KEY="0x..."
  ```
- Hoặc dùng Spring Profiles:
  ```properties
  # application-prod.properties (không commit)
  blockchain.wallet.private-key=${BLOCKCHAIN_PRIVATE_KEY}
  ```

**❌ SAI:**
- Commit private key lên Git
- Hardcode trong code
- Chia sẻ qua email/chat

### **2. Git Ignore**

Thêm vào `.gitignore`:
```
application-prod.properties
application-local.properties
*.key
*.pem
.env
```

### **3. Sử dụng Wallet riêng cho Backend**

- KHÔNG dùng wallet cá nhân
- Tạo wallet riêng chỉ để deploy contract và gửi transactions
- Chỉ giữ đủ ETH cho gas fees (~0.1-0.5 ETH)

---

## 📚 Additional Resources

- **Solidity Documentation:** https://docs.soliditylang.org
- **Web3j Documentation:** https://docs.web3j.io
- **Sepolia Testnet Explorer:** https://sepolia.etherscan.io
- **Ethereum Gas Tracker:** https://etherscan.io/gastracker
- **Remix IDE:** https://remix.ethereum.org

---

## 🆘 Support

Nếu gặp vấn đề, kiểm tra:
1. Application logs
2. Etherscan transaction details
3. MetaMask activity
4. Database blockchain_status field

Hoặc liên hệ team để được hỗ trợ.

---

**Last Updated:** 2025-11-24
**Contract Version:** 1.0.0
**Solidity Version:** ^0.8.20

pragma solidity ^0.8.20;

/// Hợp đồng ghi nhận quyên góp (off-chain tiền), lưu tổng theo projectId.
/// - Các giao dịch không chuyển ETH, chỉ ghi dữ liệu + phát event để minh bạch.
/// - Số tiền dùng đơn vị "cents" (2 chữ số thập phân): amountCents = soTien * 100.
contract CharityDonations {
    address public owner;
    mapping(address => bool) public authorized;

    // Tổng số tiền (cents) theo projectId
    mapping(uint256 => uint256) private _projectTotalCents;

    event DonationRecorded(
        uint256 indexed projectId,
        string donor,                 // địa chỉ ví hoặc tên người quyên góp (chuỗi)
        uint256 amountCents,          // số tiền dạng cents
        string currency,              // ví dụ: "VND"
        string memo,                  // nội dung/ghi chú
        address indexed operator,     // người gọi hàm (backend ví)
        uint256 timestamp
    );

    event AuthorizedUpdated(address indexed account, bool allowed);
    event OwnershipTransferred(address indexed previousOwner, address indexed newOwner);

    modifier onlyOwner() {
        require(msg.sender == owner, "Not owner");
        _;
    }

    modifier onlyAuthorized() {
        require(msg.sender == owner || authorized[msg.sender], "Not authorized");
        _;
    }

    constructor() {
        owner = msg.sender;
        emit OwnershipTransferred(address(0), msg.sender);
    }

    /// Chủ sở hữu thêm/bỏ quyền ghi on-chain cho một địa chỉ (ví backend).
    function setAuthorized(address account, bool allowed) external onlyOwner {
        authorized[account] = allowed;
        emit AuthorizedUpdated(account, allowed);
    }

    /// Chuyển quyền sở hữu.
    function transferOwnership(address newOwner) external onlyOwner {
        require(newOwner != address(0), "Zero address");
        emit OwnershipTransferred(owner, newOwner);
        owner = newOwner;
    }

    /// API 1: Ghi quyên góp (tăng tổng cho projectId và phát event).
    /// Khớp signature backend đang encode:
    /// recordDonation(uint256 projectId, string donor, uint256 amountCents, string currency, string memo)
    function recordDonation(
        uint256 projectId,
        string calldata donor,
        uint256 amountCents,
        string calldata currency,
        string calldata memo
    ) external onlyAuthorized {
        require(projectId != 0, "projectId=0");
        require(amountCents > 0, "amount=0");

        _projectTotalCents[projectId] += amountCents;

        emit DonationRecorded(
            projectId,
            donor,
            amountCents,
            currency,
            memo,
            msg.sender,
            block.timestamp
        );
    }

    /// API 2: Đọc tổng (cents) theo projectId.
    /// Khớp tên hàm backend mặc định gọi: getProjectTotalCents(uint256) returns (uint256)
    function getProjectTotalCents(uint256 projectId) external view returns (uint256) {
        return _projectTotalCents[projectId];
    }

    /// Tham khảo: số chữ số thập phân dùng trong amount (2 nghĩa là "cents").
    function centsDecimals() external pure returns (uint8) {
        return 2;
    }
}

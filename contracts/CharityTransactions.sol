// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

/**
 * @title CharityTransactions
 * @dev Smart contract for storing charity donation and disbursement records on Sepolia testnet
 */
contract CharityTransactions {

    // Contract owner (deployer)
    address public owner;

    // Donation structure
    struct Donation {
        uint256 id;                     // Database ID
        address donor;                  // Donor wallet address
        uint256 projectId;              // Project ID
        uint256 amount;                 // Donation amount (in wei or smallest unit)
        string currency;                // Currency unit (VND, ETH, etc.)
        string transactionCode;         // Transaction code from database
        string paymentMethod;           // Payment method
        uint256 timestamp;              // Block timestamp
        bool exists;                    // Check if record exists
    }

    // Disbursement structure
    struct Disbursement {
        uint256 id;                     // Database ID
        uint256 projectId;              // Project ID
        uint256 amount;                 // Disbursement amount
        string disbursementType;        // Type: TU_TRO, HOA_DONG, QUAN_LY
        string recipient;               // Recipient name
        string purpose;                 // Purpose of use
        address approver;               // Approver wallet address
        uint256 timestamp;              // Block timestamp
        bool exists;                    // Check if record exists
    }

    // Mappings for storage
    mapping(uint256 => Donation) public donations;           // donationId => Donation
    mapping(uint256 => Disbursement) public disbursements;   // disbursementId => Disbursement
    mapping(uint256 => uint256[]) public projectDonations;   // projectId => donationIds[]
    mapping(uint256 => uint256[]) public projectDisbursements; // projectId => disbursementIds[]

    // Counters
    uint256 public totalDonations;
    uint256 public totalDisbursements;

    // Events
    event DonationRecorded(
        uint256 indexed id,
        address indexed donor,
        uint256 indexed projectId,
        uint256 amount,
        string transactionCode,
        uint256 timestamp
    );

    event DisbursementRecorded(
        uint256 indexed id,
        uint256 indexed projectId,
        uint256 amount,
        string recipient,
        address approver,
        uint256 timestamp
    );

    // Modifiers
    modifier onlyOwner() {
        require(msg.sender == owner, "Only owner can call this function");
        _;
    }

    modifier donationNotExists(uint256 _id) {
        require(!donations[_id].exists, "Donation already recorded");
        _;
    }

    modifier disbursementNotExists(uint256 _id) {
        require(!disbursements[_id].exists, "Disbursement already recorded");
        _;
    }

    // Constructor
    constructor() {
        owner = msg.sender;
        totalDonations = 0;
        totalDisbursements = 0;
    }

    /**
     * @dev Record a new donation on blockchain
     * @param _id Database ID of the donation
     * @param _donor Donor wallet address
     * @param _projectId Project ID
     * @param _amount Donation amount
     * @param _currency Currency unit
     * @param _transactionCode Transaction code from database
     * @param _paymentMethod Payment method
     */
    function recordDonation(
        uint256 _id,
        address _donor,
        uint256 _projectId,
        uint256 _amount,
        string memory _currency,
        string memory _transactionCode,
        string memory _paymentMethod
    ) public onlyOwner donationNotExists(_id) {

        donations[_id] = Donation({
            id: _id,
            donor: _donor,
            projectId: _projectId,
            amount: _amount,
            currency: _currency,
            transactionCode: _transactionCode,
            paymentMethod: _paymentMethod,
            timestamp: block.timestamp,
            exists: true
        });

        projectDonations[_projectId].push(_id);
        totalDonations++;

        emit DonationRecorded(
            _id,
            _donor,
            _projectId,
            _amount,
            _transactionCode,
            block.timestamp
        );
    }

    /**
     * @dev Record a new disbursement on blockchain
     * @param _id Database ID of the disbursement
     * @param _projectId Project ID
     * @param _amount Disbursement amount
     * @param _disbursementType Type of disbursement
     * @param _recipient Recipient name
     * @param _purpose Purpose of use
     * @param _approver Approver wallet address
     */
    function recordDisbursement(
        uint256 _id,
        uint256 _projectId,
        uint256 _amount,
        string memory _disbursementType,
        string memory _recipient,
        string memory _purpose,
        address _approver
    ) public onlyOwner disbursementNotExists(_id) {

        disbursements[_id] = Disbursement({
            id: _id,
            projectId: _projectId,
            amount: _amount,
            disbursementType: _disbursementType,
            recipient: _recipient,
            purpose: _purpose,
            approver: _approver,
            timestamp: block.timestamp,
            exists: true
        });

        projectDisbursements[_projectId].push(_id);
        totalDisbursements++;

        emit DisbursementRecorded(
            _id,
            _projectId,
            _amount,
            _recipient,
            _approver,
            block.timestamp
        );
    }

    /**
     * @dev Get donation details by ID
     * @param _id Donation ID
     * @return Donation struct
     */
    function getDonation(uint256 _id) public view returns (Donation memory) {
        require(donations[_id].exists, "Donation not found");
        return donations[_id];
    }

    /**
     * @dev Get disbursement details by ID
     * @param _id Disbursement ID
     * @return Disbursement struct
     */
    function getDisbursement(uint256 _id) public view returns (Disbursement memory) {
        require(disbursements[_id].exists, "Disbursement not found");
        return disbursements[_id];
    }

    /**
     * @dev Get all donation IDs for a project
     * @param _projectId Project ID
     * @return Array of donation IDs
     */
    function getProjectDonations(uint256 _projectId) public view returns (uint256[] memory) {
        return projectDonations[_projectId];
    }

    /**
     * @dev Get all disbursement IDs for a project
     * @param _projectId Project ID
     * @return Array of disbursement IDs
     */
    function getProjectDisbursements(uint256 _projectId) public view returns (uint256[] memory) {
        return projectDisbursements[_projectId];
    }

    /**
     * @dev Check if a donation exists
     * @param _id Donation ID
     * @return bool
     */
    function donationExists(uint256 _id) public view returns (bool) {
        return donations[_id].exists;
    }

    /**
     * @dev Check if a disbursement exists
     * @param _id Disbursement ID
     * @return bool
     */
    function disbursementExists(uint256 _id) public view returns (bool) {
        return disbursements[_id].exists;
    }

    /**
     * @dev Transfer ownership of the contract
     * @param newOwner Address of new owner
     */
    function transferOwnership(address newOwner) public onlyOwner {
        require(newOwner != address(0), "New owner cannot be zero address");
        owner = newOwner;
    }
}

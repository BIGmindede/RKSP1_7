// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract SimpleStore {
    address public owner;
    mapping(uint256 => Product) public products;
    uint256 public productCount = 0;

    struct Product {
        string name;
        uint256 price;
        uint256 quantity;
    }

    event ProductAdded(uint256 productId, string name, uint256 price, uint256 quantity);
    event ProductPurchased(uint256 productId, uint256 quantity, uint256 totalCost);

    modifier onlyOwner() {
        require(msg.sender == owner, "Only the owner can call this function");
        _;
    }

    constructor() {
        owner = msg.sender;
    }

    // Добавление продукта владельцем
    function addProduct(string memory _name, uint256 _price, uint256 _quantity) public onlyOwner {
        productCount++;
        products[productCount] = Product(_name, _price, _quantity);
        emit ProductAdded(productCount, _name, _price, _quantity);
    }

    // Покупка продукта
    function purchaseProduct(uint256 _productId, uint256 _quantity) public payable {
        require(_productId > 0 && _productId <= productCount, "Invalid product ID");
        Product storage product = products[_productId];
        require(product.quantity >= _quantity, "Not enough stock available");
        uint256 totalCost = product.price * _quantity;
        require(msg.value >= totalCost, "Insufficient funds sent");

        // Обновляем количество продукта
        product.quantity -= _quantity;

        // Переводим излишек обратно покупателю, если был отправлен излишек
        if (msg.value > totalCost) {
            payable(msg.sender).transfer(msg.value - totalCost);
        }

        emit ProductPurchased(_productId, _quantity, totalCost);
    }
    // Вывод средств владельцем контракта
    function withdrawBalance() public onlyOwner {
        uint256 contractBalance = address(this).balance;
        require(contractBalance > 0, "No balance to withdraw");
        payable(owner).transfer(contractBalance);
    }
}
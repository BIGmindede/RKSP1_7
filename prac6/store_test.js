const { expect } = require("chai");
const { ethers } = require("hardhat");

describe("SimpleStore Contract", function () {
  let simpleStore;
  let owner;
  let addr1;
  let addr2;
  let productId;

  beforeEach(async function () {
    [owner, addr1, addr2] = await ethers.getSigners();
    const SimpleStore = await ethers.getContractFactory("SimpleStore");
    simpleStore = await SimpleStore.deploy();
    await simpleStore.deployed();
  });

  // Тест на добавление продукта
  it("should add a product successfully", async function () {
    await simpleStore.connect(owner).addProduct("Product A", ethers.utils.parseEther("0.1"), 10); // Цена 0.1 ETH
    const product = await simpleStore.products(1);
    expect(product.name).to.equal("Product A");
    expect(product.price).to.equal(ethers.utils.parseEther("0.1"));
    expect(product.quantity).to.equal(10);
    await expect(
      simpleStore.connect(owner).addProduct("Product B", ethers.utils.parseEther("0.15"), 5)
    )
      .to.emit(simpleStore, "ProductAdded")
      .withArgs(2, "Product B", ethers.utils.parseEther("0.15"), 5);
  });
  // Тест на покупку продукта
  it("should allow a user to purchase a product", async function () {
    await simpleStore.connect(owner).addProduct("Product A", ethers.utils.parseEther("0.1"), 10); // 0.1 ETH цена
    productId = 1;
    await simpleStore.connect(addr1).purchaseProduct(productId, 2, {
      value: ethers.utils.parseEther("0.2"), // Отправляем 0.2 ETH (0.1 * 2 = 0.2 ETH)
    });
    const product = await simpleStore.products(productId);
    expect(product.quantity).to.equal(8); // 10 - 2
    await expect(
      simpleStore.connect(addr1).purchaseProduct(productId, 1, {
        value: ethers.utils.parseEther("0.1"), // 0.1 * 1 = 0.1 ETH
      })
    )
      .to.emit(simpleStore, "ProductPurchased")
      .withArgs(productId, 1, ethers.utils.parseEther("0.1"));
  });


  // Тест на покупку продукта с недостаточным количеством на складе
  it("should fail if there is not enough stock available", async function () {
    await simpleStore.connect(owner).addProduct("Product A", ethers.utils.parseEther("0.1"), 2);
    await expect(
      simpleStore.connect(addr1).purchaseProduct(1, 3, {
        value: ethers.utils.parseEther("0.3"), // Пробуем купить 3 при 2 на складе
      })
    ).to.be.revertedWith("Not enough stock available");
  });
});

package com.ecom.library.service.impl;

import com.ecom.library.dto.CartItemDto;
import com.ecom.library.dto.ProductDto;
import com.ecom.library.dto.ShoppingCartDto;
import com.ecom.library.model.CartItem;
import com.ecom.library.model.Customer;
import com.ecom.library.model.Product;
import com.ecom.library.model.ShoppingCart;
import com.ecom.library.repository.CartItemRepository;
import com.ecom.library.repository.ShoppingCartRepository;
import com.ecom.library.service.CustomerService;
import com.ecom.library.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/*  ----------------------------------Shopping cart Service Implementation----------------------------------------    */

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository cartRepository;
    private final CartItemRepository itemRepository;
    private final CustomerService customerService;

    /*  ----------------------------------Add item to cart----------------------------------------------------    */
    @Override
    public ShoppingCart addItemToCart(ProductDto productDto, int quantity, String username) {
        //Find customer by username
        Customer customer = customerService.findByUsername(username);
        //Get his cart
        ShoppingCart shoppingCart = customer.getCart();
        //If no cart exists , create one
        if(shoppingCart == null){
            shoppingCart = new ShoppingCart();
        }
        //Create set of items list
        Set< CartItem> cartItemList = shoppingCart.getCartItems();
        //Search cart item in list of items
        CartItem cartItem = find(cartItemList, productDto.getId());
        //Make product object
        Product product = transfer(productDto);

        double unitPrice = productDto.getCostPrice();

        int itemQuantity = 0;
        //If no items is found in cart , make hashset
        if (cartItemList == null) {
            cartItemList = new HashSet<>();
            cartInfo(quantity, shoppingCart, cartItemList, cartItem, product, unitPrice);
        } else {
            cartInfo(quantity, shoppingCart, cartItemList, cartItem, product, unitPrice);
        }
        //Put back the updated list in cart
        shoppingCart.setCartItems(cartItemList);

        //Update the prices accordingly
        double totalPrice = totalPrice(shoppingCart.getCartItems());
        int totalItem = totalItem(shoppingCart.getCartItems());

        shoppingCart.setTotalPrice(totalPrice);
        shoppingCart.setTotalItems(totalItem);
        shoppingCart.setCustomer(customer);

        return cartRepository.save(shoppingCart);
    }

    /*  ----------------------------------Helper method----------------------------------------------------    */
    private void cartInfo(int quantity, ShoppingCart shoppingCart, Set<CartItem> cartItemList, CartItem cartItem, Product product, double unitPrice) {

        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(shoppingCart);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(unitPrice);
            cartItem.setCart(shoppingCart);
            cartItemList.add(cartItem);
            itemRepository.save(cartItem);
        } else {
           int itemQuantity = cartItem.getQuantity() + quantity;
            cartItem.setQuantity(itemQuantity);
            itemRepository.save(cartItem);
        }
    }

    /*  ----------------------------------Update cart----------------------------------------------------    */
    @Override
    public ShoppingCart updateCart(ProductDto productDto, int quantity, String username) {
        return getShoppingCart(productDto, username);
    }

    /*  ----------------------------------Remove item from cart----------------------------------------------------    */
    @Override
    public ShoppingCart removeItemFromCart(ProductDto productDto, String username) {
        return getShoppingCart(productDto, username);
    }

    /*  ----------------------------------Get shopping cart----------------------------------------------------    */
    private ShoppingCart getShoppingCart(ProductDto productDto, String username) {
        //Find customer by username
        Customer customer = customerService.findByUsername(username);
        //Grt his cart
        ShoppingCart shoppingCart = customer.getCart();
        //get items in the cart
        Set<CartItem> cartItemList = shoppingCart.getCartItems();
        //Find specific item in list
        CartItem item = find(cartItemList, productDto.getId());
        //Removes the item
        cartItemList.remove(item);
        //Delete it from repository as well
        itemRepository.delete(item);
        //Update prices
        double totalPrice = totalPrice(cartItemList);
        int totalItem = totalItem(cartItemList);
        shoppingCart.setCartItems(cartItemList);
        shoppingCart.setTotalPrice(totalPrice);
        shoppingCart.setTotalItems(totalItem);
        //Save shopping cart
        return cartRepository.save(shoppingCart);
    }

    /*  ----------------------------------Add item to cart session----------------------------------------------------    */
    @Override
    public ShoppingCartDto addItemToCartSession(ShoppingCartDto cartDto, ProductDto productDto, int quantity) {
        //Extract Item Dto from product Dto
        CartItemDto cartItem = findInDTO(cartDto, productDto.getId());
        //if Dto is null - no item
        if (cartDto == null) {
            cartDto = new ShoppingCartDto();
        }
        //Create set to store items in Dto
        Set<CartItemDto> cartItemList = cartDto.getCartItems();
        double unitPrice = productDto.getCostPrice();
        int itemQuantity = 0;
        //if no items are in the list
        if (cartItemList == null) {
            //make a new hashset and store the item
            cartItemList = new HashSet<>();
            cartAttributes(cartDto, productDto, quantity, cartItem, cartItemList, unitPrice);
        } else {
            //else store it in already existing list
            cartAttributes(cartDto, productDto, quantity, cartItem, cartItemList, unitPrice);
        }
        System.out.println("here");
        //Update Dto item with newly modified item list
        cartDto.setCartItems(cartItemList);
        //Update the prices accordingly
        double totalPrice = totalPriceDto(cartItemList);
        int totalItem = totalItemDto(cartItemList);
        cartDto.setTotalPrice(totalPrice);
        cartDto.setTotalItems(totalItem);
        System.out.println(cartDto.getTotalItems());
        System.out.println(cartDto.getTotalPrice());
        System.out.println("success");
        return cartDto;
    }

    /*  ----------------------------------Add item----------------------------------------------------    */
    private void cartAttributes(ShoppingCartDto cartDto, ProductDto productDto, int quantity, CartItemDto cartItem, Set<CartItemDto> cartItemList, double unitPrice) {
        int itemQuantity;
        if (cartItem == null) {
            cartItem = new CartItemDto();
            cartItem.setProduct(productDto);
            cartItem.setCart(cartDto);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(unitPrice);
            cartItemList.add(cartItem);
            System.out.println("add");
        } else {
            itemQuantity = cartItem.getQuantity() + quantity;
            cartItem.setQuantity(itemQuantity);
        }
    }

    /*  ----------------------------------Update cart by quantity----------------------------------------------------    */
    @Override
    public ShoppingCartDto updateCartSession(ShoppingCartDto cartDto, ProductDto productDto, int quantity) {
        //create set to store all incoming items in Dto
        Set<CartItemDto> cartItemList = cartDto.getCartItems();
        //Find specific item from Dto
        CartItemDto item = findInDTO(cartDto, productDto.getId());
        int itemQuantity = item.getQuantity() + quantity;
        int totalItem = totalItemDto(cartItemList);
        double totalPrice = totalPriceDto(cartItemList);
        item.setQuantity(itemQuantity);
        //Set modified price
        cartDto.setCartItems(cartItemList);
        cartDto.setTotalPrice(totalPrice);
        cartDto.setTotalItems(totalItem);
        System.out.println(cartDto.getTotalItems());
        return cartDto;
    }

    /*  ----------------------------------Remove item from cart----------------------------------------------------    */
    @Override
    public ShoppingCartDto removeItemFromCartSession(ShoppingCartDto cartDto, ProductDto productDto, int quantity) {
        Set<CartItemDto> cartItemList = cartDto.getCartItems();
        CartItemDto item = findInDTO(cartDto, productDto.getId());
        cartItemList.remove(item);
        double totalPrice = totalPriceDto(cartItemList);
        int totalItem = totalItemDto(cartItemList);
        cartDto.setCartItems(cartItemList);
        cartDto.setTotalPrice(totalPrice);
        cartDto.setTotalItems(totalItem);
        System.out.println(cartDto.getTotalItems());
        return cartDto;
    }

    /*  ----------------------------------Combine carts----------------------------------------------------    */
    @Override
    public ShoppingCart combineCart(ShoppingCartDto cartDto, ShoppingCart cart) {
        if (cart == null) {
            cart = new ShoppingCart();
        }
        Set<CartItem> cartItems = cart.getCartItems();
        if (cartItems == null) {
            cartItems = new HashSet<>();
        }
        Set<CartItem> cartItemsDto = convertCartItem(cartDto.getCartItems(), cart);
        cartItems.addAll(cartItemsDto);
        double totalPrice = totalPrice(cartItems);
        int totalItems = totalItem(cartItems);
        cart.setTotalItems(totalItems);
        cart.setCartItems(cartItems);
        cart.setTotalPrice(totalPrice);
        return cart;
    }

    /*  ----------------------------------Delete cart by id----------------------------------------------------    */
    @Override
    public void deleteCartById(Long id) {
        //Get cart by id
        ShoppingCart shoppingCart = cartRepository.getById(id);
        //remove every item from cart
        for (CartItem cartItem : shoppingCart.getCartItems()) {
            itemRepository.deleteById(cartItem.getId());
        }
        //Modify the customer and price factor
        shoppingCart.setCustomer(null);
        shoppingCart.getCartItems().clear();
        shoppingCart.setTotalPrice(0);
        shoppingCart.setTotalItems(0);
        cartRepository.save(shoppingCart);
    }

    /*  ----------------------------------Find methods----------------------------------------------------    */
    @Override
    public ShoppingCart getCart(String username) {
        Customer customer = customerService.findByUsername(username);
        ShoppingCart cart = customer.getCart();
        return cart;
    }

    //Get item by id from cartItem
    private CartItem find(Set<CartItem> cartItems, long productId) {
        if (cartItems == null) {
            return null;
        }
        CartItem cartItem = null;
        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == productId) {
                cartItem = item;
            }
        }
        return cartItem;
    }

    private CartItemDto findInDTO(ShoppingCartDto shoppingCart, long productId) {
        //if no cart exists
        if (shoppingCart == null) {
            return null;
        }
        CartItemDto cartItem = null;
        //get specific item by using id from cart
        for (CartItemDto item : shoppingCart.getCartItems()) {
            if (item.getProduct().getId() == productId) {
                cartItem = item;
            }
        }
        return cartItem;
    }

    /*  ----------------------------------Total calculations methods----------------------------------------------------    */
    private int totalItem(Set<CartItem> cartItemList) {
        int totalItem = 0;
        for (CartItem item : cartItemList) {
            totalItem += item.getQuantity();
        }
        return totalItem;
    }

    private double totalPrice(Set<CartItem> cartItemList) {
        double totalPrice = 0.0;
        for (CartItem item : cartItemList) {
            totalPrice += item.getUnitPrice() * item.getQuantity();
        }
        return totalPrice;
    }

    private int totalItemDto(Set<CartItemDto> cartItemList) {
        int totalItem = 0;
        for (CartItemDto item : cartItemList) {
            totalItem += item.getQuantity();
        }
        return totalItem;
    }

    private double totalPriceDto(Set<CartItemDto> cartItemList) {
        double totalPrice = 0;
        for (CartItemDto item : cartItemList) {
            totalPrice += item.getUnitPrice() * item.getQuantity();
        }
        return totalPrice;
    }

    /*  ----------------------------------Helping method----------------------------------------------------    */
    private Product transfer(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setCurrentQuantity(productDto.getCurrentQuantity());
        product.setCostPrice(productDto.getCostPrice());
        product.setSalePrice(productDto.getSalePrice());
        product.setDescription(productDto.getDescription());
        product.setImage(productDto.getImage());
        product.set_activated(productDto.isActivated());
        product.set_deleted(productDto.isDeleted());
        product.setCategory(productDto.getCategory());
        return product;
    }

    /*  ----------------------------------Convert cart item method----------------------------------------------------    */
    private Set<CartItem> convertCartItem(Set<CartItemDto> cartItemDtos, ShoppingCart cart) {
        Set<CartItem> cartItems = new HashSet<>();
        for (CartItemDto cartItemDto : cartItemDtos) {
            CartItem cartItem = new CartItem();
            cartItem.setQuantity(cartItemDto.getQuantity());
            cartItem.setProduct(transfer(cartItemDto.getProduct()));
            cartItem.setUnitPrice(cartItemDto.getUnitPrice());
            cartItem.setId(cartItemDto.getId());
            cartItem.setCart(cart);
            cartItems.add(cartItem);
        }
        return cartItems;
    }

}

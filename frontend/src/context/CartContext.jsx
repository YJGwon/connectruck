import React, {createContext, useState, useEffect} from 'react';

export const CartContext = createContext();

export const CartProvider = ({children}) => {
    const [cartItems, setCartItems] = useState([]);

    useEffect(() => {
        const rawCartItems = localStorage.getItem('cartItems');
        if (rawCartItems !== null) {
            setCartItems(JSON.parse(rawCartItems));
        }
    }, []);

    useEffect(() => {
        console.log('use effect', cartItems);
        localStorage.setItem('cartItems', JSON.stringify(cartItems));
    }, [cartItems]);

    const addToCart = (menu) => {
        // Check if the menu item already exists in the cart
        const existingItem = cartItems.find(item => item.id === menu.id);
        if (existingItem) {
            // If the item already exists, update its quantity
            const updatedItems = cartItems.map(item => {
                if (item.id === menu.id) {
                    return {
                        ...item,
                        quantity: item.quantity + 1
                    };
                }
                return item;
            });
            setCartItems(updatedItems);
        } else {
            // If it's a new item, add it to the cart with a quantity of 1
            setCartItems(cartItems.concat({
                ...menu,
                quantity: 1
            }));
        }
    };

    const changeQuantity = (itemId, newQuantity) => {
        setCartItems(prevItems => prevItems.map(
            item => item.id === itemId
                ? {
                    ...item,
                    quantity: newQuantity
                }
                : item
        ));
    };

    const removeFromCart = (itemId) => {
        const updatedItems = cartItems.filter(item => item.id !== itemId);
        setCartItems(updatedItems);
    };

    const calculateSubtotal = () => {
        return cartItems.reduce((total, item) => total + item.price * item.quantity, 0);
    };

    const clearCart = () => {
        setCartItems([]);
    };

    return (
        <CartContext.Provider
            value={{
                cartItems,
                addToCart,
                changeQuantity,
                removeFromCart,
                calculateSubtotal,
                clearCart
            }}>
            {children}
        </CartContext.Provider>
    );
};

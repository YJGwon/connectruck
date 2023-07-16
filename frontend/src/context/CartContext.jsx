import React, {createContext, useState, useEffect} from 'react';

import {fetchApi} from '../function/CustomFetch';

export const CartContext = createContext();

export const CartProvider = ({children}) => {
    const [cartItems, setCartItems] = useState([]);
    const [truckId, setTruckId] = useState(0);

    useEffect(() => {
        const rawCartItems = localStorage.getItem('cartItems');
        if (rawCartItems !== null) {
            setCartItems(JSON.parse(rawCartItems));
            setTruckId(localStorage.getItem('cartTruckId'));
        }
    }, []);

    useEffect(() => {
        localStorage.setItem('cartItems', JSON.stringify(cartItems));
    }, [cartItems]);

    useEffect(() => {
        localStorage.setItem('cartTruckId', truckId);
    }, [truckId]);

    const addToCart = (menu, truckIdOfMenu) => {
        // Check if the menu item already exists in the cart
        if (cartItems.length !== 0 && truckIdOfMenu !== truckId) {
            alert('같은 푸드트럭의 메뉴만 담을 수 있습니다.');
            return;
        }

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
        setTruckId(truckIdOfMenu);
        alert('장바구니에 담았습니다.');
    };

    const changeQuantity = (itemId, newQuantity) => {
        if (newQuantity < 0) {
            alert('수량은 0보다 작을 수 없습니다.');
            return;
        }

        if (newQuantity === 0) {
            removeFromCart(itemId);
            return;
        }

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
        setTruckId(0);
    };

    const calculateSubtotal = () => {
        return cartItems.reduce((total, item) => total + item.price * item.quantity, 0);
    };

    const checkOut = async (phone) => {
        const url = `${process.env.REACT_APP_API_URL}/api/orders`;
        const data = {
            truckId,
            phone,
            menus: cartItems.map((item) => ({menuId: item.id, quantity: item.quantity}))
        };
        const requestInfo = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        };
        const onSuccess = () => {
            setCartItems([]);
            alert('주문이 완료되었습니다.');
        };
        fetchApi({url, requestInfo}, onSuccess);
    };

    return (
        <CartContext.Provider
            value={{
                cartItems,
                truckId,
                addToCart,
                changeQuantity,
                removeFromCart,
                calculateSubtotal,
                checkOut
            }}>
            {children}
        </CartContext.Provider>
    );
};

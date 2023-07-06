import React, { useState } from 'react';
import { Typography, Button, Container, Box, List, ListItem, ListItemText, IconButton } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';

export default function ServiceCart() {
    const [cartItems, setCartItems] = useState([
        {
            id: 1,
            name: 'Food Item 1',
            price: 8000,
            quantity: 2
        }, {
            id: 2,
            name: 'Food Item 2',
            price: 12000,
            quantity: 1
        },
        // Add more cart items as needed
    ]);

    const handleQuantityChange = (itemId, newQuantity) => {
        setCartItems(prevItems => prevItems.map(
            item => item.id === itemId
                ? {
                    ...item,
                    quantity: newQuantity
                }
                : item
        ));
    };

    const handleRemoveItem = (itemId) => {
        setCartItems(prevItems => prevItems.filter(item => item.id !== itemId));
    };

    const calculateSubtotal = () => {
        return cartItems.reduce((total, item) => total + item.price * item.quantity, 0);
    };

    return (
        <Container>
            <Box mt={3}>
                <Typography variant="h4" component="h1" align="center">
                    장바구니
                </Typography>
            </Box>

            <Box mt={3}>
                <List>
                    {
                        cartItems.length === 0
                            ? (
                                <ListItem>
                                    <ListItemText primary="주문할 메뉴를 담아주세요."/>
                                </ListItem>
                            )
                            : (cartItems.map(item => (
                                <ListItem key={item.id}>
                                    <ListItemText primary={item.name} secondary={`${item.price}원`}/>
                                    <Button
                                        variant="outlined"
                                        size="small"
                                        onClick={() => handleQuantityChange(item.id, item.quantity - 1)}>
                                        -
                                    </Button>
                                    <span>{item.quantity}</span>
                                    <Button
                                        variant="outlined"
                                        size="small"
                                        onClick={() => handleQuantityChange(item.id, item.quantity + 1)}>
                                        +
                                    </Button>
                                    <IconButton aria-label="delete" onClick={() => handleRemoveItem(item.id)}>
                                        <DeleteIcon/>
                                    </IconButton>
                                </ListItem>
                            )))
                    }
                </List>
            </Box>

            <Box mt={3}>
                <Typography variant="h6" component="p">
                    총 주문 금액: {calculateSubtotal()}원
                </Typography>
                <Button variant="contained" color="primary" disabled={cartItems.length === 0}>
                    주문하기
                </Button>
            </Box>
        </Container>
    );
};

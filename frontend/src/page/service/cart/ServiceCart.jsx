import React, {useState, useContext} from 'react';
import {
    Typography,
    Button,
    Container,
    Box,
    List,
    ListItem,
    ListItemText,
    IconButton
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';

import {CartContext} from '../../../context/CartContext';

export default function ServiceCart() {
    const {cartItems, changeQuantity, removeFromCart, calculateSubtotal, clearCart} = useContext(CartContext);

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
                                        onClick={() => changeQuantity(item.id, item.quantity - 1)}>
                                        -
                                    </Button>
                                    <span>{item.quantity}</span>
                                    <Button
                                        variant="outlined"
                                        size="small"
                                        onClick={() => changeQuantity(item.id, item.quantity + 1)}>
                                        +
                                    </Button>
                                    <IconButton aria-label="delete" onClick={() => removeFromCart(item.id)}>
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
                <Button
                    variant="contained"
                    color="primary"
                    onClick={() => clearCart()}
                    disabled={cartItems.length === 0}>
                    주문하기
                </Button>
            </Box>
        </Container>
    );
};

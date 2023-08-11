import React, {useState, useContext, useEffect} from 'react';
import {
    Typography,
    Button,
    Container,
    Box,
    List,
    ListItem,
    ListItemText,
    IconButton,
    Modal,
    TextField
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';

import {CartContext} from '../../../context/CartContext';
import {fetchData} from '../../../function/CustomFetch';

import './ServiceCart.css';

export default function ServiceCart() {
    const {
        cartItems,
        truckId,
        changeQuantity, 
        removeFromCart, 
        calculateSubtotal, 
        checkOut
    } = useContext(CartContext);

    const [truckName, setTruckName] = useState('');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [phoneNumber, setPhoneNumber] = useState('');

    useEffect(() => {
        if (!truckId || truckId == 0) {
            setTruckName('');
            return;
        }
        fetchTruck();
    }, [truckId]);

    const fetchTruck = () => {
        const url = `${process.env.REACT_APP_API_URL}/api/trucks/${truckId}`;
        const onSuccess = (data) => {
            setTruckName(data.name);
        };

        fetchData({url}, onSuccess);
    }

    const handleCheckOut = () => {
        setIsModalOpen(true);
    };

    const handleCloseModal = () => {
        setIsModalOpen(false);
        setPhoneNumber('');
    };

    const handleConfirmOrder = () => {
        if (phoneNumber.trim() === '') {
            alert('휴대폰 번호를 입력해주세요.');
            return;
        }

        checkOut(phoneNumber);
        handleCloseModal();
    };

    return (
        <Container>
            <Box mt={3}>
                <Typography variant="h4" component="h1" align="center">
                    장바구니
                </Typography>
                <Typography variant="h5" component="h1" align="center">
                    {truckName && `${truckName}`}
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
                    onClick={handleCheckOut}
                    disabled={cartItems.length === 0}>
                    주문하기
                </Button>
            </Box>
            <Modal open={isModalOpen} onClose={handleCloseModal}>
                <Box className="modalContainer">
                    <Typography variant="h5" component="h2" className="modalTitle">
                        휴대폰 번호를 입력해주세요.
                    </Typography>
                    <TextField
                        label="휴대폰 번호"
                        variant="outlined"
                        fullWidth={true}
                        value={phoneNumber}
                        onChange={(e) => setPhoneNumber(e.target.value)}
                        className="modalTextInput"/>

                    <Button
                        variant="contained"
                        color="primary"
                        onClick={handleConfirmOrder}
                        disabled={!phoneNumber}
                        className="modalConfirmButton">
                        주문하기
                    </Button>
                </Box>
            </Modal>
        </Container>
    );
};

import React, {useState, useEffect, useContext} from 'react';
import {
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper,
    Modal,
    Box,
    Fade,
    Pagination,
    Stack,
    Button,
    Chip
} from '@mui/material';

import {UserContext} from '../../../context/UserContext';
import {fetchData, fetchApi} from '../../../function/CustomFetch';
import {OrderDetailTable} from '../../../component/table/OrderDetailTable';
import {BoldTableCell} from '../../../component/table/BoldTableCell';

import './OwnerOrderList.css';

export const OwnerOrderList = ({selectedStatus, newOrders, handleOnOrderClick}) => {
    const [orders, setOrders] = useState([]);
    const [orderDetail, setOrderDetail] = useState(null);
    const [page, setPage] = useState(1);
    const [size, setSize] = useState(10);
    const [totalPages, setTotalPages] = useState(1);

    const {isInitialized, accessToken} = useContext(UserContext);

    const orderStatus = [
        {
            value: 'CREATED',
            button: '조리 시작',
            action: 'accept'
        }, 
        {
            value: 'COOKING',
            button: '조리 완료',
            action: 'finish-cooking'
        }, 
        {
            value: 'COOKED',
            button: '픽업 완료',
            action: 'complete'
        }, 
        {
            value: 'COMPLETE'
        }, 
        {
            value: 'CANCELED'
        }
    ];

    useEffect(() => {
        fetchOrders();
    }, [isInitialized, page]);

    useEffect(() => {
        setPage(1);
        setTotalPages(1);
        fetchOrders();
    }, [isInitialized, selectedStatus]);

    useEffect(() => {
        if (selectedStatus === 0 && page === 1) {
            fetchOrders();
        }
    }, [newOrders]);

    const calculateSubtotal = () => {
        return orderDetail.menus.reduce((total, menu) => total + menu.price * menu.quantity, 0);
    };

    const handleOnRowClick = (order) => {
        handleOnOrderClick(order.id);
        openModal(order);
    }

    const openModal = (order) => {
        fetchOrderDetails(order.id);
    };

    const closeModal = () => {
        setOrderDetail(null);
    };

    const fetchOrders = () => {
        if (!isInitialized) {
            return;
        }

        const url = `${process.env.REACT_APP_API_URL}/api/owner/orders/my?page=${page - 1}&size=${size}&status=${orderStatus[selectedStatus].value}`;
        const requestInfo = {
            headers: {
                Authorization: `Bearer ${accessToken}`,
            }
        };
        const onSuccess = (data) => {
            setOrders(data.orders);
            setTotalPages(data.page.totalPages);
        };

        fetchData({ url, requestInfo }, onSuccess);
    };

    const fetchOrderDetails = (orderId) => {
        const url = `${process.env.REACT_APP_API_URL}/api/orders/${orderId}`;
        const onSuccess = (data) => {
            setOrderDetail(data);
        };

        fetchData({url}, onSuccess);
    };

    const handlePaging = (event, value) => {
        setPage(value); 
    };

    const processOrder = async (action) => {
        const url = `${process.env.REACT_APP_API_URL}/api/owner/orders/${orderDetail.id}/${action}`;
        const requestInfo = {
            method: 'POST',
            headers: {
                Authorization: `Bearer ${accessToken}`
            }
        };
        const onSuccess = () => {
            alert('처리되었습니다.');
            closeModal();
            fetchOrders();
        };
        fetchApi({url, requestInfo}, onSuccess);
    };

    return (
        <div>
            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell></TableCell>
                            <BoldTableCell>주문 일시</BoldTableCell>
                            <BoldTableCell>연락처</BoldTableCell>
                            <BoldTableCell>상태</BoldTableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {
                            orders.map((order) => (
                                <TableRow 
                                    key={order.id} 
                                    hover={true} 
                                    onClick={() => handleOnRowClick(order)} 
                                    sx={{
                                        '&:hover': {cursor: 'pointer'},
                                    }}>
                                    <TableCell>{newOrders.includes(order.id) && <Chip label="new" color="primary" />}</TableCell>
                                    <TableCell>{order.createdAt}</TableCell>
                                    <TableCell>{order.phone}</TableCell>
                                    <TableCell>{order.status}</TableCell>
                                </TableRow>
                            ))
                        }
                    </TableBody>
                </Table>
            </TableContainer>

            <Stack spacing={2} alignItems="center">
                <Pagination 
                    showFirstButton={true}
                    showLastButton={true}
                    count={totalPages} 
                    page={page} 
                    onChange={handlePaging}/>
            </Stack>

            <Modal
                open={orderDetail !== null}
                onClose={closeModal}
                closeAfterTransition={true}>
                <Fade in={orderDetail !== null}>
                    <Box component={Paper}>
                        {orderDetail && <OrderDetailTable orderDetail={orderDetail}/>}
                        <Stack spacing={2} direction="column" sx={{ p: 2, justifyContent: 'center' }}>
                            {
                                selectedStatus !== 3 && selectedStatus !== 4 &&
                                <Stack spacing={2} direction="row" sx={{ p: 2, justifyContent: 'center' }}>
                                        <Button variant="contained" onClick={() => processOrder(orderStatus[selectedStatus].action)}>
                                            {orderStatus[selectedStatus].button}
                                        </Button>
                                        <Button variant="contained" onClick={() => processOrder('cancel')} color="error">
                                            주문 취소
                                        </Button>
                                </Stack>
                            }
                            <Button variant="outlined" onClick={() => closeModal()}>닫기</Button>
                        </Stack>
                    </Box>
                </Fade>
            </Modal>
        </div>
    );
};

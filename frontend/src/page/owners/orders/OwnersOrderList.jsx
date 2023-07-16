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
    Button
} from '@mui/material';

import {UserContext} from '../../../context/UserContext';
import {BoldTableCell} from '../../../component/table/BoldTableCell';

import './OwnersOrderList.css';

export const OwnersOrderList = ({selectedStatus}) => {
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

    const calculateSubtotal = () => {
        return orderDetail.menus.reduce((total, menu) => total + menu.price * menu.quantity, 0);
    };

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

        fetch(url, {
            headers: {
              Authorization: `Bearer ${accessToken}`,
            },
          })
            .then(async response => {
                const data = await response.json();

                if (response.ok) {
                    return data;
                } else {
                    throw new Error(`api error(${data.title}): ${data.detail}`);
                }
            })
            .then(data => {
                setOrders(data.orders);
                setTotalPages(data.page.totalPages);
            })
            .catch(error => {
                console.error('Error fetching order data:', error);
                if (error.message.startsWith('api error')) {
                    alert(error.message);
                } else {
                    alert('주문 목록을 불러오지 못하였습니다');
                }
            });
    };

    const fetchOrderDetails = (orderId) => {
        const url = `${process.env.REACT_APP_API_URL}/api/orders/${orderId}`;

        fetch(url)
            .then(async response => {
                const data = await response.json();

                if (response.ok) {
                    return data;
                } else {
                    throw new Error(`api error(${data.title}): ${data.detail}`);
                }
            })
            .then(data => {
                setOrderDetail(data);
            })
            .catch(error => {
                console.error('Error fetching order detail data:', error);
                if (error.message.startsWith('api error')) {
                    alert(error.message);
                } else {
                    alert('주문 상세정보를 불러오지 못하였습니다');
                }
            });
    };

    const handlePaging = (event, value) => {
        setPage(value); 
    };

    const processOrder = async (action) => {
        const url = `${process.env.REACT_APP_API_URL}/api/owner/orders/${orderDetail.id}/${action}`;

        try {
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    Authorization: `Bearer ${accessToken}`
                }
            });

            if (response.ok) {
                alert('처리되었습니다.');
                closeModal();
                fetchOrders();
            } else {
                const data = await response.json();
                throw new Error(`api error(${data.title}): ${data.detail}`);
            }
        } catch (error) {
            console.error('Error processing order:', error);
            if (error.message.startsWith('api error')) {
                alert(error.message);
            } else {
                alert('주문을 처리하지 못하였습니다.');
            }
        }
    };

    return (
        <div>
            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
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
                                    onClick={() => openModal(order)} 
                                    sx={{
                                        '&:hover': {
                                        cursor: 'pointer'
                                        }
                                    }}>
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
                        <TableContainer>
                            <h2>주문 상세</h2>
                            <h5>주문 일시: {orderDetail?.createdAt}</h5>
                            <h5>연락처: {orderDetail?.phone}</h5>
                            <h5>상태: {orderDetail?.status}</h5>
                            <h4>메뉴</h4>
                            <Table>
                                <TableHead>
                                    <TableRow>
                                        <BoldTableCell>메뉴 이름</BoldTableCell>
                                        <BoldTableCell>수량</BoldTableCell>
                                        <BoldTableCell>금액</BoldTableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {orderDetail?.menus.map((menu) => (
                                    <TableRow key={menu.id}>
                                        <TableCell>{menu.name}</TableCell>
                                        <TableCell>{menu.quantity}</TableCell>
                                        <TableCell>{menu.price * menu.quantity}</TableCell>
                                    </TableRow>
                                    ))}
                                    <TableRow>
                                        <TableCell></TableCell>
                                        <BoldTableCell>총액</BoldTableCell>
                                        <TableCell>{orderDetail && calculateSubtotal()}</TableCell>
                                    </TableRow>
                                    {/* Render total here */}
                                </TableBody>
                            </Table>
                        </TableContainer>
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

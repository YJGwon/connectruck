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
    Fade,
    Pagination,
    Stack
} from '@mui/material';

import {UserContext} from '../../../context/UserContext';
import './OwnersOrderList.css';

export const OwnersOrderList = () => {
    const [orders, setOrders] = useState([]);
    const [orderDetail, setOrderDetail] = useState(null);
    const [page, setPage] = useState(1);
    const [size, setSize] = useState(10);
    const [totalPages, setTotalPages] = useState(1);

    const {isInitialized, accessToken} = useContext(UserContext);

    useEffect(() => {
        if (isInitialized) {
            fetchOrders();
        }
    }, [isInitialized, page]);

    const openModal = (order) => {
        fetchOrderDetails(order.id);
    };

    const closeModal = () => {
        setOrderDetail(null);
    };

    const fetchOrders = () => {
        const url = `${process.env.REACT_APP_API_URL}/api/owner/trucks/my/orders?page=${page - 1}&size=${size}`;

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

    return (
        <div className='orders-container'>
            <h1>주문 관리</h1>
            <h2>주문 목록</h2>
            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>주문 일시</TableCell>
                            <TableCell>연락처</TableCell>
                            <TableCell>상태</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {
                            orders.map((order) => (
                                <TableRow key={order.id} onClick={() => openModal(order)}>
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
                    <div className="modal">
                        <TableContainer component={Paper}>
                            <h2>주문 상세</h2>
                            <Table>
                                <TableBody>
                                    <TableRow>
                                        <TableCell>주문 일시:</TableCell>
                                        <TableCell>{orderDetail?.createdAt}</TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell>연락처:</TableCell>
                                        <TableCell>{orderDetail?.phone}</TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell>상태:</TableCell>
                                        <TableCell>{orderDetail?.status}</TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell colSpan={2}>메뉴</TableCell>
                                    </TableRow>
                                    {orderDetail?.menus.map((menu) => (
                                    <TableRow key={menu.id}>
                                        <TableCell>{menu.name}</TableCell>
                                        <TableCell>{menu.quantity}</TableCell>
                                    </TableRow>
                                    ))}
                                    <TableRow>
                                        <TableCell colSpan={2}>총액</TableCell>
                                    </TableRow>
                                    {/* Render total here */}
                                </TableBody>
                            </Table>
                        </TableContainer>
                    </div>
                </Fade>
            </Modal>
        </div>
    );
};

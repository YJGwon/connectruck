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
    Fade
} from '@mui/material';

import {UserContext} from '../../../context/UserContext';
import './OwnersOrderList.css';

export const OwnersOrderList = () => {
    const [orders, setOrders] = useState([]);
    const [selectedOrder, setSelectedOrder] = useState(null);
    const [page, setPage] = useState(0);
    const [size, setSize] = useState(20);
    const [totalPages, setTotalPages] = useState(0);

    const {isInitialized, accessToken} = useContext(UserContext);

    useEffect(() => {
        if (isInitialized) {
            fetchOrders();
        }
    }, [isInitialized]);

    const openModal = (order) => {
        setSelectedOrder(order);
    };

    const closeModal = () => {
        setSelectedOrder(null);
    };

    const fetchOrders = () => {
        const url = `${process.env.REACT_APP_API_URL}/api/owner/trucks/my/orders?page=${page}&size=${size}`;

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
                setPage(data.page.currentPage + 1);
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
                                <TableRow key={order.orderNumber} onClick={() => openModal(order)}>
                                    <TableCell>{order.createdAt}</TableCell>
                                    <TableCell>{order.phone}</TableCell>
                                    <TableCell>{order.status}</TableCell>
                                </TableRow>
                            ))
                        }
                    </TableBody>
                </Table>
            </TableContainer>

            <Modal
                open={selectedOrder !== null}
                onClose={closeModal}
                closeAfterTransition={true}>
                <Fade in={selectedOrder !== null}>
                    <div className="modal">
                        <TableContainer component={Paper}>
                          <h2>주문 상세</h2>
                            <Table>
                                <TableBody>
                                    <TableRow>
                                        <TableCell>주문 일시:</TableCell>
                                        <TableCell>{selectedOrder?.createdAt}</TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell>연락처:</TableCell>
                                        <TableCell>{selectedOrder?.phone}</TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell>상태:</TableCell>
                                        <TableCell>{selectedOrder?.status}</TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell colSpan={2}>메뉴</TableCell>
                                    </TableRow>
                                    {/* Render order items here */}
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

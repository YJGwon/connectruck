import React, {useState, useEffect} from 'react';
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

import './OwnersOrderList.css';

export const OwnersOrderList = () => {
    const [orders, setOrders] = useState([]);
    const [selectedOrder, setSelectedOrder] = useState(null);

    useEffect(() => {
        fetchOrders();
    }, []);

    const openModal = (order) => {
        setSelectedOrder(order);
    };

    const closeModal = () => {
        setSelectedOrder(null);
    };

    const fetchOrders = () => {
        const fetchedOrders = [
            {
                createdAt: '2023-07-12 08:30 AM',
                phone: '01000000000',
                status: '접수 대기'
            }, {
                createdAt: '2023-07-12 09:15 AM',
                phone: '01000000001',
                status: '조리중'
            }, {
                createdAt: '2023-07-12 09:45 AM',
                phone: '010000000002',
                status: '조리 완료'
            }, {
                createdAt: '2023-07-12 10:30 AM',
                phone: '01000000003',
                status: '픽업 완료'
            }
        ];
        setOrders(fetchedOrders);
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

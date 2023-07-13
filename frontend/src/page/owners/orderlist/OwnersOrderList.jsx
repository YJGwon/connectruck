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
import {BoldTableCell} from '../../../component/table/BoldTableCell';

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
            <h1>주문 접수</h1>
            <h2>주문 목록</h2>
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
                    </div>
                </Fade>
            </Modal>
        </div>
    );
};

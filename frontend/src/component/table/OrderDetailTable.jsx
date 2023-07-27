import React from 'react';
import {
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow
} from '@mui/material';

import { BoldTableCell } from './BoldTableCell';

export const OrderDetailTable = ({orderDetail}) => {
    const calculateSubtotal = () => {
        return orderDetail.menus.reduce((total, menu) => total + menu.price * menu.quantity, 0);
    };

    return (
        <TableContainer>
            <h5>주문 일시: {orderDetail.createdAt}</h5>
            <h5>연락처: {orderDetail.phone}</h5>
            <h5>상태: {orderDetail.status}</h5>
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
                    {orderDetail.menus.map((menu) => (
                    <TableRow key={menu.id}>
                        <TableCell>{menu.name}</TableCell>
                        <TableCell>{menu.quantity}</TableCell>
                        <TableCell>{menu.price * menu.quantity}</TableCell>
                    </TableRow>
                    ))}
                    <TableRow>
                        <TableCell></TableCell>
                        <BoldTableCell>총액</BoldTableCell>
                        <TableCell>{calculateSubtotal()}</TableCell>
                    </TableRow>
                </TableBody>
            </Table>
        </TableContainer>
    );
}
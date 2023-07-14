import React, {useState} from 'react';
import {Box, Tabs, Tab} from '@mui/material';

export const OwnersOrderAccept = () => {
    const [orderStatus, setOrderStatus] = React.useState('created');

    const handleChange = (event, newValue) => {
        setOrderStatus(newValue);
    };

    return (
        <Box sx={{ width: '100%', margin: '1%' }}>
            <h1>주문 접수</h1>
            <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
                <Tabs value={orderStatus} onChange={handleChange}>
                    <Tab label='접수 대기' value='created' />
                    <Tab label='조리중' value='cooking' />
                    <Tab label='픽업 대기' value='cooked'/>
                    <Tab label='픽업 완료' value='complete' />
                    <Tab label='취소됨' value='canceled' />
                </Tabs>
            </Box>
            <h5>{orderStatus}</h5>
        </Box>
    );
}

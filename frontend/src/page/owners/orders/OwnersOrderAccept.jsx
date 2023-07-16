import React, {useState, useEffect} from 'react';
import {Box, Tabs, Tab} from '@mui/material';

import {OwnersOrderList} from './OwnersOrderList';

export const OwnersOrderAccept = () => {
    const [selectedTab, setSelectedTab] = useState(0);

    const handleChange = (event, selected) => {
        setSelectedTab(selected);
    };

    return (
        <Box sx={{ width: '100%', margin: '1%' }}>
            <h1>주문 접수</h1>
            <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
                <Tabs value={selectedTab} onChange={handleChange}>
                    <Tab label='접수 대기' />
                    <Tab label='조리중' />
                    <Tab label='조리 완료'/>
                    <Tab label='픽업 완료' />
                    <Tab label='취소됨' />
                </Tabs>
            </Box>
            <OwnersOrderList selectedStatus={selectedTab} />
        </Box>
    );
}

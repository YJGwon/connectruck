import React, {useState} from 'react';
import {Container, Button, Stack} from '@mui/material';

import TopBar from '../../component/topbar/TopBar';

import {ServiceOrderTrackingModal} from '../service/ordertracking/ServiceOrderTrackingModal';

export default function Index() {
    const [openModal, setOpenModal] = useState(false);

    const title = "Connectruck 🚚";
    const root = "/";

    return (
        <React.Fragment>
            <TopBar title={title} root={root}/>
            <Container>
                <h4>행사 푸드트럭 주문 시스템을 이용하려면 행사에 제공된 url로 접속해주세요.</h4>
                <Stack spacing={2} direction="row">
                    <Button variant="contained" href="/owner" sx={{marginRight: '1px'}}>사장님 페이지</Button>
                    <Button variant="contained" onClick={() => setOpenModal(true)}>주문 조회</Button>
                </Stack>
            </Container>
            <ServiceOrderTrackingModal open={openModal} onClose={() => setOpenModal(false)} />
        </React.Fragment>
    );
}

import React from 'react';
import {Container, Button} from '@mui/material';

import {requestNotificationPermission} from '../../../function/BrowserNotification';

import './OwnerMain.css';

export const OwnerMain = () => {

    return (
        <Container>
            <h4>주문 push 알림 활성화를 위해 사이트의 브라우저 알림 권한을 허용해주세요.</h4>
            <Button variant="contained" onClick={() => requestNotificationPermission()}>알림 권한 허용</Button>
        </Container>
    );
}

import React from 'react';
import {Button} from '@mui/material';

import TopBar from '../../component/topbar/TopBar';

export default function Index() {
    const title = 'Connectruck 🚚';
    const root = "/";

    return (
        <div>
            <TopBar title={title} root={root}/>
            <div className="container">
                <h4>행사 푸드트럭 주문 시스템을 이용하려면 행사에 제공된 url로 접속해주세요.</h4>
                <Button variant="contained" href="/owners">사장님 페이지</Button>
            </div>
        </div>
    );
}

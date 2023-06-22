import React from 'react';
import TopBar from '../../../component/topbar/TopBar';

import {Routes, Route} from 'react-router-dom';

export default function OwnersMain() {
    const title = '사장님 서비스 🚚';
    const home = "/owners";
    const buttons = [
        {
            link: '/',
            name: 'connectruck'
        }
    ];

    return (
        <div>
            <TopBar title={title} home={home} buttons={buttons}/>
            <div className="container">
                <Routes>
                    <Route exact="exact" path="/" element='사장님 페이지'/>
                </Routes>
            </div>
        </div>
    );
}

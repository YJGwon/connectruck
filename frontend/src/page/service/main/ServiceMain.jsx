import React from 'react';
import TopBar from '../../../component/topbar/TopBar';

import {Routes, Route} from 'react-router-dom';
import ServiceTruckList from '../trucklist/ServiceTruckList';

export default function ServiceMain() {
    const title = 'Connectruck 🚚';
    const home = "/";
    const buttons = [
        {
            link: '/owners',
            name: '사장님 서비스'
        }
    ];

    return (
        <div>
            <TopBar title={title} home={home} buttons={buttons}/>
            <div className="container">
                <Routes>
                    <Route path="/event/:eventId" element={<ServiceTruckList />}/>
                </Routes>
            </div>
        </div>
    );
}

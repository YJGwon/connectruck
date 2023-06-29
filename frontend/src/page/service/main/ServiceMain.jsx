import React from 'react';
import {Routes, Route, useParams} from 'react-router-dom';

import TopBar from '../../../component/topbar/TopBar';
import ServiceTruckList from '../trucklist/ServiceTruckList';

export default function ServiceMain() {
    const {eventId} = useParams();

    const title = 'Connectruck 🚚';
    const root = `/event/${eventId}`;

    return (
        <div>
            <TopBar title={title} root={root} />
            <div className="container">
                <Routes>
                    <Route exact='exact' path='/' element={<ServiceTruckList eventId={eventId}/>}/>
                </Routes>
            </div>
        </div>
    );
}

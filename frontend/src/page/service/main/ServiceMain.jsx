import React from 'react';
import {Routes, Route, useParams} from 'react-router-dom';

import TopBar from '../../../component/topbar/TopBar';
import ServiceTruckList from '../trucklist/ServiceTruckList';
import ServiceTruck from '../truck/ServiceTruck';

export default function ServiceMain() {
    const {eventId} = useParams();

    const title = 'Connectruck 🚚';
    const root = `/events/${eventId}`;

    return (
        <div>
            <TopBar title={title} root={root} />
            <div className="container">
                <Routes>
                    <Route exact='exact' path='/' element={<ServiceTruckList eventId={eventId}/>}/>
                    <Route path='/trucks/:truckId/*' element={<ServiceTruck />} />
                </Routes>
            </div>
        </div>
    );
}

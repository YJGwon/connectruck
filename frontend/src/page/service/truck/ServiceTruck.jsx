import React from 'react';
import { Routes, Route, useParams } from 'react-router-dom';

import ServiceMenuList from '../menulist/ServiceMenuList';

export default function ServiceTruck() {
    const { truckId } = useParams();

    return (
        <div>
            <h1>{`트럭 ${truckId} 상세 페이지`}</h1>
            <Routes>
                <Route exact='exact' path='/' element={<ServiceMenuList truckId={truckId} />} />
            </Routes>
        </div>
    );
}

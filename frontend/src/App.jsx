import React from 'react';
import './App.css';

import {BrowserRouter, Routes, Route} from 'react-router-dom';
import ServiceMain from './page/service/main/ServiceMain';
import OwnersMain from './page/owners/main/OwnersMain';

export default function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route exact="exact" path="/*" element={<ServiceMain />}/>
                <Route path="/owners/*" element={<OwnersMain />}/>
            </Routes>
        </BrowserRouter>
    );
}

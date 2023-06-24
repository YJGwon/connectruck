import React from 'react';
import './App.css';

import {UserProvider} from './context/UserContext';
import {BrowserRouter, Routes, Route} from 'react-router-dom';
import ServiceMain from './page/service/main/ServiceMain';
import OwnersMain from './page/owners/main/OwnersMain';

export default function App() {
    return (
        <BrowserRouter>
            <UserProvider>
                <Routes>
                    <Route exact="exact" path="/*" element={<ServiceMain />}/>
                    <Route path="/owners/*" element={<OwnersMain />}/>
                </Routes>
            </UserProvider>
        </BrowserRouter>
    );
}

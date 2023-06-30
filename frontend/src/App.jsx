import React, {useContext} from 'react';
import {UserContext} from './context/UserContext';
import {BrowserRouter, Routes, Route, Navigate} from 'react-router-dom';

import Index from './page/index/Index';
import ServiceMain from './page/service/main/ServiceMain';
import OwnersMain from './page/owners/main/OwnersMain';

import './App.css';

export default function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route exact="exact" path="/" element={<Index />}/>
                <Route path="/events/:eventId/*" element={<ServiceMain />}/>
                <Route path="/owners/*" element={<OwnersMain />}/>
                <Route path="/logout" element={<Logout />}/>
            </Routes>
        </BrowserRouter>
    );
}

function Logout() {
    const {logout} = useContext(UserContext);
    logout();
    return <Navigate to='/'/>;
}

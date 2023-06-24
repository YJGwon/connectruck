import React, {useContext} from 'react';
import './App.css';

import {UserContext} from './context/UserContext';
import {BrowserRouter, Routes, Route, Navigate} from 'react-router-dom';
import ServiceMain from './page/service/main/ServiceMain';
import OwnersMain from './page/owners/main/OwnersMain';

export default function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route exact="exact" path="/*" element={<ServiceMain />}/>
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

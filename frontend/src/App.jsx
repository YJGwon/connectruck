import './App.css';
import TopBar from './component/topbar/TopBar';

import {BrowserRouter, Routes, Route} from 'react-router-dom';
import TruckList from './page/trucklist/TruckList';
import OwnersMain from './page/owners/main/OwnersMain';

function App() {
    return (
        <BrowserRouter>
            <TopBar/>
            <div className="container">
                <Routes>
                    <Route exact="exact" path="/" element={<TruckList />}/>
                    <Route path="/owners" element={<OwnersMain />}/>
                </Routes>
            </div>
        </BrowserRouter>
    );
}

export default App;

import logo from './logo.svg';
import './App.css';
import TopBar from './component/topbar/TopBar';
import TruckList from './page/trucklist/TruckList';

function App() {
  return (
    <div>
      <TopBar />
      <div className="container">
        <TruckList />
      </div>
    </div>  
  );
}

export default App;

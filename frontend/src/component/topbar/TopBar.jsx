import React from 'react';
import {AppBar, Toolbar, Typography, Link} from '@mui/material';
import './TopBar.css'; // Import the CSS file

function TopBar() {
    return (
        <AppBar position="static" className="topbar">
            <Toolbar>
                <Typography variant="h6" component="div" className="topbar__title">
                    <Link href="/" color="inherit" underline="none">
                        Connectruck 🚚
                    </Link>
                </Typography>
                <nav>
                    <Link href="/owners" color="inherit" underline="none" className="topbar__link">
                        사장님 서비스
                    </Link>
                </nav>
            </Toolbar>
        </AppBar>
    );
}

export default TopBar;

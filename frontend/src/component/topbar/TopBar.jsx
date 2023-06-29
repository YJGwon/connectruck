import React from 'react';
import {AppBar, Toolbar, Typography, Link} from '@mui/material';

import './TopBar.css';

export default function TopBar({title, root}) {

    return (
        <AppBar position="static" className="topbar">
            <Toolbar>
                <Typography variant="h6" component="div" className="topbar__title">
                    <Link href={root} color="inherit" underline="none">
                        {title}
                    </Link>
                </Typography>
            </Toolbar>
        </AppBar>
    );
}

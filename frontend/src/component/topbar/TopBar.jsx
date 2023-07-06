import React from 'react';
import {AppBar, Toolbar, Typography, Link} from '@mui/material';

import './TopBar.css';

export default function TopBar({title, root, buttons}) {

    return (
        <AppBar position="static" className="topbar">
            <Toolbar>
                <Typography variant="h6" component="div" className="topbar__title">
                    <Link href={root} color="inherit" underline="none">
                        {title}
                    </Link>
                </Typography>
                <nav>
                    {buttons &&
                        buttons.map((button, index) => (
                            <Link
                                key={index}
                                href={button.link}
                                color="inherit" underline="none" className="topbar__link">
                                {button.name}
                            </Link>
                        ))
                    }
                </nav>            
            </Toolbar>
        </AppBar>
    );
}

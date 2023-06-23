import React from 'react';
import {AppBar, Toolbar, Typography, Link} from '@mui/material';
import './TopBar.css';

export default function TopBar(props) {
    const title = props.title;
    const home = props.home;
    const buttons = props.buttons;

    return (
        <AppBar position="static" className="topbar">
            <Toolbar>
                <Typography variant="h6" component="div" className="topbar__title">
                    <Link href={home} color="inherit" underline="none">
                        {title}
                    </Link>
                </Typography>
                <nav>
                    {
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

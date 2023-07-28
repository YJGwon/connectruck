import React, {useState} from 'react';
import {
    AppBar,
    Toolbar,
    Link,
    Typography,
    IconButton,
    Drawer,
    Box,
    List
} from '@mui/material';
import {Menu} from '@mui/icons-material';

export default function TobBar({title, root, icon, buttons}) {

    const [openMenu, setOpenMenu] = useState(false);

    const toggleMenu = (open) => (event) => {
        if (event.type === 'keydown' && (event.key === 'Tab' || event.key === 'Shift')) {
            return;
        }

        setOpenMenu(open);
    };
  
    return (
        <AppBar position="static">
            <Toolbar>
                <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                    <Link href={root} color="inherit" underline="none">
                        {title}
                    </Link>
                </Typography>
                <div>
                    {buttons && 
                    <IconButton
                        size="large"
                        edge="end"
                        color="inherit"
                        onClick={toggleMenu(true)}>
                        {icon ? icon : <Menu/>}
                    </IconButton>}
                    <Drawer
                        anchor="right"
                        open={openMenu}
                        onClose={toggleMenu(false)}>
                        <Box
                            sx={{ width: 250 }}
                            onKeyDown={toggleMenu(false)}>
                            <List>
                                {buttons}
                            </List>
                        </Box>
                    </Drawer>
                </div>
            </Toolbar>
        </AppBar>
    );
}

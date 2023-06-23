import React from 'react';
import {styled} from '@mui/system';
import {Drawer, List, ListItemButton, ListItemText} from '@mui/material';

const drawerWidth = 240;

const StyledDrawer = styled(Drawer)(({theme}) => ({
    width: drawerWidth,
    flexShrink: 0,
    '& .MuiDrawer-paper': {
        width: drawerWidth,
        height: '100vh',
        position: 'relative'
    }
}));

export default function SideBar(props) {
    const buttons = props.buttons;

    return (
        <div className='sidebar'>
            <StyledDrawer variant="permanent">
                <List>
                    {
                        buttons.map((button, index) => (
                            <ListItemButton key={index} href={button.link}>
                                <ListItemText primary={button.name}/>
                            </ListItemButton>
                        ))
                    }
                </List>
            </StyledDrawer>
        </div>
    );
};

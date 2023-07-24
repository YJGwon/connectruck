import React from 'react';
import {styled} from '@mui/system';
import {Drawer, List} from '@mui/material';

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

export default function SideBar({buttons}) {
    return (
        <div className='sidebar'>
            <StyledDrawer variant="permanent">
                <List>
                    {buttons}
                </List>
            </StyledDrawer>
        </div>
    );
};

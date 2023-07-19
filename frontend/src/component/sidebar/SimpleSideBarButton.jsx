import React from 'react';
import {ListItemButton, ListItemText} from '@mui/material';

export default function SimpleSideBarButton({key, link, name}) {
    return (
        <ListItemButton 
            key={key}
            href={link}>
            <ListItemText primary={name}/>
        </ListItemButton>
    );
};

import React from 'react';
import {ListItemButton, ListItemText, Badge} from '@mui/material';

export default function BadgedSideBarButton({key, link, name, badgeContent}) {
    return (
        <ListItemButton 
            key={key}
            href={link}>
            <ListItemText primary={name}/>
            <Badge badgeContent={badgeContent} color='info'/>
        </ListItemButton>
    );
};

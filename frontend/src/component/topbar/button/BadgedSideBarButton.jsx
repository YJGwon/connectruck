import React from 'react';
import {ListItem, ListItemButton, ListItemText, Badge} from '@mui/material';

export default function BadgedSideBarButton({index, link, name, badgeContent}) {
    return (
        <ListItem key={index} disablePadding={true}>
            <ListItemButton href={link}>
                <ListItemText primary={name}/>
                <Badge badgeContent={badgeContent} color='info'/>
            </ListItemButton>
        </ListItem>
    );
};

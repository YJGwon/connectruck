import React, {useState, useEffect, useContext} from 'react';
import {
    Box, 
    Typography, 
    List, 
    ListItem, 
    ListItemText, 
    Stack,
    Switch,
    FormControlLabel,
    IconButton
} from '@mui/material';
import {Edit} from '@mui/icons-material';

import {UserContext} from '../../../context/UserContext';
import {fetchData} from '../../../function/CustomFetch';

export const OwnerMenuList = () => {
    const [menus, setMenus] = useState([]);

    const {isInitialized, accessToken} = useContext(UserContext);

    useEffect(() => {
        fetchMenus();
    }, [isInitialized]);

    const fetchMenus = () => {
        if (!isInitialized) {
            return;
        }

        const url = `${process.env.REACT_APP_API_URL}/api/owner/menus/my`;
        const requestInfo = {
            headers: {
                Authorization: `Bearer ${accessToken}`,
            }
        };
        const onSuccess = (data) => {
            setMenus(data.menus);
        };
        fetchData({url, requestInfo}, onSuccess);
    }

    return (
        <Box sx={{ width: '100%', margin: '1%' }}>
            <h1>메뉴 관리</h1>
            <List>
                {
                    menus.map((menu, index) => (
                        <ListItem key={index}>
                            <ListItemText 
                                primary={menu.name} 
                                secondary={
                                    <React.Fragment>
                                        {menu.description}
                                        <IconButton size="small">
                                            <Edit fontSize="inherit" />
                                        </IconButton>
                                    </React.Fragment>
                                }/>
                            <Stack direction="row" spacing={2}>
                                <Typography>{`${menu.price}원`}</Typography>
                                <FormControlLabel
                                    control={<Switch checked={menu.soldOut} color="error" />}
                                    label="품절"
                                    labelPlacement="top"
                                />
                            </Stack>
                        </ListItem>
                    ))
                }
            </List>
        </Box>
    );
}
import {
    TableCell,
    styled,
    tableCellClasses
} from '@mui/material';

export const BoldTableCell = styled(TableCell)(({theme}) => ({
    [`&.${tableCellClasses.root}`]: {
      fontSize: 16,
      fontWeight: 700
    }
}));

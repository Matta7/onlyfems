import React from "react";
import { Outlet } from "react-router-dom";
import Navbar from "../Navbar";

import '../../styles/Admin/Index.css'
import { SnackMessageType } from "../../types/entityType";
import SnackMessage from "../SnackMessage";

type NavbarContainerPropsType = {
  snackMessage: SnackMessageType | null,
  setSnack: (smt: SnackMessageType | null) => void,
}

const NavbarContainer: React.FC<NavbarContainerPropsType> = ({
  snackMessage,
  setSnack,
}) => {
  return (
    <div>
      <Navbar setSnack={setSnack} />

      { snackMessage ? 
        <SnackMessage 
          snackMessage={snackMessage}
          fullTop={snackMessage.fullTop ?? false}
          closeAction={() => setSnack(null)} 
        /> : 
        null
      }

      <div className="content">
        <Outlet/>
      </div>
    </div>
  )
}

export default NavbarContainer
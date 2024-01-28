import React from "react";
import useGetUser from "../../hooks/useGetUser";
import { Outlet, useNavigate } from "react-router-dom";
import LoadingCircle from "../LoadingCircle";
import { isAdmin } from "../../utils/user";
import AdminSidebar from "./AdminSidebar";

import '../../styles/Admin/Index.css'
import clientPath from "../../utils/clientPath";


const Administration: React.FC = () => {
  const { user } = useGetUser();
  const navigate = useNavigate();

  if (!user) {
    return <LoadingCircle />
  }

  if (!isAdmin(user)) {
    navigate(clientPath.HOME)
  }

  return (
    <div className="administration flex">
      <AdminSidebar />
      <Outlet/>
    </div>
  )
}

export default Administration
import { useEffect, useState } from "react"
import { Link, useNavigate } from "react-router-dom"
import clientPath from "../../../utils/clientPath"

import '../../../styles/Admin/Sidebar.css'

const AdminSidebar = () => {
  const [currentTab, setCurrentTab] = useState(clientPath.ADMIN_IMAGES)
  const navigate = useNavigate()

  useEffect(() => {
    switch (window.location.pathname) {
      case `/admin/${clientPath.ADMIN_USERS}`:
        setCurrentTab(clientPath.ADMIN_USERS)
        break
      default:
        setCurrentTab(clientPath.ADMIN_IMAGES)
        navigate(clientPath.ADMIN_IMAGES)
    }
  }, [navigate])

  return (
    <div className="admin-sidebar flex">
      <Link 
        className={`admin-link ${currentTab === clientPath.ADMIN_IMAGES && 'current'}`}
        to={clientPath.ADMIN_IMAGES} 
        onClick={() => setCurrentTab(clientPath.ADMIN_IMAGES)}
      >
        Images
      </Link>
      <Link 
        className={`admin-link ${currentTab === clientPath.ADMIN_USERS && 'current'}`}
        to={clientPath.ADMIN_USERS} 
        onClick={() => setCurrentTab(clientPath.ADMIN_USERS)}
      >
        Utilisateurs
      </Link>
    </div>
  )
}

export default AdminSidebar
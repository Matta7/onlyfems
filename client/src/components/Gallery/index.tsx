import { useNavigate } from "react-router-dom"
import useGetUser from "../../hooks/useGetUser"
import { isVIP } from "../../utils/user"
import LoadingCircle from "../LoadingCircle"
import clientPath from "../../utils/clientPath"

import '../../styles/Gallery.css'
import { ImageType } from "../../types/entityType"
import { useQuery } from "react-query"
import { allImagesQuery } from "../../apis/queries"
import BigImageCard from "./BigImageCard"
import PaginationTab from "../PaginationTab"
import { useState } from "react"

type GalleryPropsType = {
  vipContent?: boolean
}

const Gallery: React.FC<GalleryPropsType> = ({
  vipContent = false,
}) => {
  // Pagination
  const [currentPage, setCurrentPage] = useState(1)
  const [totalPage, setTotalPage] = useState(1)
  const [totalElements, setTotalElements] = useState(0)
  const pageSize = 10
  
  const navigate = useNavigate()
  const { user } = useGetUser()

  const { data: images, isLoading } = useQuery<ImageType[]>({
    queryKey: ['all-images', vipContent, totalPage, currentPage],
    queryFn: async () => {
      const data = await allImagesQuery(vipContent, currentPage, pageSize)
      setTotalPage(data.totalPages)
      setTotalElements(data.totalElements)
      return data.images
    }
  })
  
  if (!user || isLoading) {
    return <LoadingCircle />
  }

  if (vipContent && !isVIP(user)) {
    navigate(clientPath.GALLERY)
  }

  const renderedImages = images ? 
    images.map(
      (image, index) => <BigImageCard key={image.id} even={index % 2 === 0} image={image} />
    ) : []

  return (
    <div className="gallery-container flex">
      <div className="gallery flex">
        {renderedImages.length > 0 ?
          <>
            <div className="images flex">
              {renderedImages}
            </div>
            <div className="pagination-container">
              <PaginationTab 
                currentPage={currentPage} 
                totalPages={totalPage} 
                totalElements={totalElements}
                pageSize={pageSize}
                setPage={setCurrentPage} 
              />
            </div>
          </> : 
          <div className="no-images flex">
            <img src={`${process.env.PUBLIC_URL}/assets/admin-no-content.png`} alt="no-content" />
            <p className="title">Il n'y a aucun élément dans le portfolio !</p>
            <p className="subtitle">(Pour l'instant... 😉)</p>
          </div>
        }
      </div>
    </div>
  )
}

export default Gallery
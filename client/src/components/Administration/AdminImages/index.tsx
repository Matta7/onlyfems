import React, { useState } from "react"

import '../../../styles/Admin/Images.css'
import NewImageModal from "./NewImageModal";
import { useQuery } from "react-query";
import { ImageType, SnackMessageType } from "../../../types/entityType";
import { allImagesQuery, deleteImageQuery } from "../../../apis/queries";
import LoadingCircle from "../../LoadingCircle";
import ImageCard from "./ImageCard";
import EditImageModal from "./EditImageModal";
import PaginationTab from "../../PaginationTab";

type AdminImagesPropsType = {
  setSnack: (snackMessage: SnackMessageType) => void
}

const AdminImages: React.FC<AdminImagesPropsType> = ({
  setSnack,
}) => {
  // Pagination
  const [currentPage, setCurrentPage] = useState(1)
  const [totalPage, setTotalPage] = useState(1)
  const [totalElements, setTotalElements] = useState(0)
  const pageSize = 10

  const [showNewImageModal, setShowNewImageModal] = useState(false);
  const [showEditImageModal, setShowEditImageModal] = useState(false);
  const [imageToEdit, setImageToEdit] = useState<ImageType | null>(null);

  const { data: allImages, refetch: refetchAllImages } = useQuery<ImageType[]>({
    queryKey: ['all-images', totalPage, currentPage],
    queryFn: async () => {
      const data = await allImagesQuery(true, currentPage, pageSize)
      setTotalPage(data.totalPages)
      setTotalElements(data.totalElements)
      return data.images
    }
  })

  if (!allImages) {
    return (
      <div className="empty flex">
        <LoadingCircle />
      </div>
    ) 
  }
  
  const editImage = (image: ImageType) => {
    setShowEditImageModal(true)
    setImageToEdit(image)
  }

  const removeEditImageModal = () => {
    setShowEditImageModal(false)
    setImageToEdit(null)
  }

  const deleteImage = async (image: ImageType) => {
    await deleteImageQuery(image.id);
    refetchAllImages()
    setSnack({
      type: 'success',
      message: 'Image supprimée avec succès !',
      fullTop: false
    })
  }

  const renderedImages = allImages.map(image => 
    <ImageCard 
      key={`${image.id}/${image.description}/${image.public}/${image.name}`} 
      image={image} 
      editCallback={() => editImage(image)}
      deleteCallback={() => deleteImage(image)}
      setSnack={setSnack}
    />
  )

  return (
    <div className="admin-images flex">
      {showNewImageModal && <NewImageModal closeCallback={() => setShowNewImageModal(false)} setSnack={setSnack} refetch={refetchAllImages} />}
      {showEditImageModal && <EditImageModal image={imageToEdit as ImageType} closeCallback={removeEditImageModal} setSnack={setSnack} refetch={refetchAllImages} />}
      {
        renderedImages.length > 0 ?
        ( 
          <>
            <div className="show-modal-btn-container">
              <button 
                className="show-modal-btn"
                onClick={() => setShowNewImageModal(true)}
              >
                Nouvelle image
              </button>
            </div>
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
          </>
        ) : (
          <div className="no-images flex">
            <img src={`${process.env.PUBLIC_URL}/assets/admin-no-content.png`} alt="no-content" />
            <p className="title">Vous n'avez aucun élément dans votre portfolio !</p>
            <p className="subtitle">Commencez dès à présent à ajouter des images en cliquant sur le bouton ci-dessous</p>
            <button 
              className="show-modal-btn"
              onClick={() => setShowNewImageModal(true)}
            >
              Nouvelle image
            </button>
          </div>
        )
      }
    </div>
  )
}

export default AdminImages
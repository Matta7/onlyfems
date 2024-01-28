import '../../styles/PaginationTab.css'

type PaginationTabPropsType = {
  currentPage: number,
  totalPages: number,
  totalElements: number,
  pageSize: number,
  setPage: (newPage: number) => void,
}

const PaginationTab: React.FC<PaginationTabPropsType> = ({
  currentPage,
  totalPages,
  totalElements,
  pageSize,
  setPage
}) => {

  return (
    <div className="pagination-tab flex">
      <div className="navigation flex">
        <button className={``} disabled={currentPage === 1} onClick={() => setPage(1)}>
          {"<<"}
        </button>
        <button className={``} disabled={currentPage === 1} onClick={() => setPage(currentPage - 1)}>
          {"<"}
        </button>
        <div className="current-page flex">
          <p>{currentPage}</p>
        </div>
       
        <button className={``} disabled={currentPage === totalPages} onClick={() => setPage(currentPage + 1)}>
          {">"}
        </button>
        <button className={``} disabled={currentPage === totalPages} onClick={() => setPage(totalPages)}>
          {">>"}
        </button>
      </div>
      <div className="progression">
        <p>{1 + pageSize * (currentPage - 1)} - {(pageSize * currentPage) > totalElements ? totalElements : (pageSize * currentPage)} sur {totalElements}</p>
      </div>
    </div>
  )
}

export default PaginationTab
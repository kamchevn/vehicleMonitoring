import React from "react";
import Pagination from "react-bootstrap/Pagination";

const PagePagination = ({ page, totalPages, onPageChange }) => {

    const pages = [];

    for (let i = 1; i <= totalPages; i++) {
        pages.push(
            <Pagination.Item
                key={i}
                active={i === page}
                onClick={() => onPageChange(i)}
            >
                {i}
            </Pagination.Item>
        );
    }

    return (
        <div className="d-flex justify-content-center mt-4 mb-4">
            <Pagination>
                <Pagination.First
                    disabled={page === 1}
                    onClick={() => onPageChange(1)}
                />
                <Pagination.Prev
                    disabled={page === 1}
                    onClick={() => onPageChange(page - 1)}
                />

                {pages}

                <Pagination.Next
                    disabled={page === totalPages}
                    onClick={() => onPageChange(page + 1)}
                />
                <Pagination.Last
                    disabled={page === totalPages}
                    onClick={() => onPageChange(totalPages)}
                />
            </Pagination>
        </div>
    );
};

export default PagePagination;

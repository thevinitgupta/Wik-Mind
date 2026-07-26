export interface PageRequest extends Pagination, Sorting {}

export interface Pagination {
  page?: number;
  size?: number;
}
export interface Sorting {
  sort?: string;
  direction?: SortOrder;
}

export enum SortOrder {
  ASC = "asc",
  DESC = "desc",
}

export interface Page<T> {
  content: T[];
  page: {
    size: number;
    number: number;
    totalElements: number;
    totalPages: number;
  };
}

export function buildPageParams(request: PageRequest) {
    return {
        page: request.page ?? 0,
        size: request.size ?? 10,
        sort: request.sort
            ? `${request.sort},${request.direction ?? SortOrder.DESC}`
            : undefined,
    };
}


export interface Page<T> {
  content: T[];

  number: number;
  size: number;

  totalElements: number;
  totalPages: number;

  first: boolean;
  last: boolean;
  empty: boolean;

  numberOfElements: number;
}


export function formatDate(
    date: string | Date,
    options?: Intl.DateTimeFormatOptions
  ) {
    return new Intl.DateTimeFormat("en-IN", {
      day: "numeric",
      month: "short",
      year: "numeric",
      ...options,
    }).format(new Date(date));
  }